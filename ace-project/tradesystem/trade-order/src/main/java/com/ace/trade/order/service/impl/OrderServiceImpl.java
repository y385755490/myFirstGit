package com.ace.trade.order.service.impl;

import com.ace.trade.common.api.ICouponApi;
import com.ace.trade.common.api.IGoodsApi;
import com.ace.trade.common.api.IUserApi;
import com.ace.trade.common.constants.MQEnums;
import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.exception.AceMQException;
import com.ace.trade.common.exception.AceOrderException;
import com.ace.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.ace.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.ace.trade.common.protocol.coupon.QueryCouponReq;
import com.ace.trade.common.protocol.coupon.QueryCouponRes;
import com.ace.trade.common.protocol.goods.QueryGoodsReq;
import com.ace.trade.common.protocol.goods.QueryGoodsRes;
import com.ace.trade.common.protocol.goods.ReduceGoodsNumberReq;
import com.ace.trade.common.protocol.goods.ReduceGoodsNumberRes;
import com.ace.trade.common.protocol.mq.CancelOrderMQ;
import com.ace.trade.common.protocol.order.ConfirmOrderReq;
import com.ace.trade.common.protocol.order.ConfirmOrderRes;
import com.ace.trade.common.protocol.user.ChangeUserMoneyReq;
import com.ace.trade.common.protocol.user.ChangeUserMoneyRes;
import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;
import com.ace.trade.common.rocketmq.AceMQProducer;
import com.ace.trade.common.util.IDGenerator;
import com.ace.trade.entity.TradeOrder;
import com.ace.trade.mapper.TradeOrderMapper;
import com.ace.trade.order.service.IOrderService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IGoodsApi goodsApi;

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private ICouponApi couponApi;

    @Autowired
    private IUserApi userApi;

    @Autowired
    private AceMQProducer aceMQProducer;

    @Transactional
    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq) {
        ConfirmOrderRes confirmOrderRes = new ConfirmOrderRes();
        try{
            QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
            queryGoodsReq.setGoodsId(confirmOrderReq.getGoodsId());
            QueryGoodsRes queryGoodsRes = goodsApi.queryGoods(queryGoodsReq);
            //1.检查校验
            checkConfirmOrderReq(confirmOrderReq,queryGoodsRes);
            //2.创建不可见订单
            String orderId = saveNoConfirmOrder(confirmOrderReq);
            //3.调用远程服务，扣优惠券/扣库存/扣余额，如果调用成功->更改订单状态可见，失败->发送MQ消息，进行取消订单
            callRemoteService(orderId,confirmOrderReq);
            confirmOrderRes.setOrderId(orderId);
        }catch (Exception e){
            confirmOrderRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            confirmOrderRes.setRetInfo(e.getMessage());
        }

        return confirmOrderRes;
    }

    /**
     * 调用远程服务，扣优惠券/扣库存/扣余额，如果调用成功->更改订单状态可见，失败->发送MQ消息，进行取消订单
     * @param confirmOrderReq
     */
    private void callRemoteService(String orderId,ConfirmOrderReq confirmOrderReq) {
        try{
            //调用优惠券
            if (StringUtils.isNotBlank(confirmOrderReq.getCouponId())){
                ChangeCouponStatusReq changeCouponStatusReq = new ChangeCouponStatusReq();
                changeCouponStatusReq.setCouponId(confirmOrderReq.getCouponId());
                changeCouponStatusReq.setIsUsed(TradeEnums.YesNoEnum.YES.getCode());
                changeCouponStatusReq.setOrderId(orderId);
                ChangeCouponStatusRes changeCouponStatusRes = couponApi.changeCouponStatus(changeCouponStatusReq);
                if(!StringUtils.equals(changeCouponStatusRes.getRetCode(),TradeEnums.RetEnum.SUCCESS.getCode())){
                    throw new Exception("优惠券使用失败！");
                }
            }
            //扣余额
            if (confirmOrderReq.getMoneyPaid() != null
                    && confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO) == 1){
                ChangeUserMoneyReq changeUserMoneyReq = new ChangeUserMoneyReq();
                changeUserMoneyReq .setOrderId(orderId);
                changeUserMoneyReq.setUserMoney(confirmOrderReq.getMoneyPaid());
                changeUserMoneyReq.setUserId(confirmOrderReq.getUserId());
                changeUserMoneyReq.setMoneyLogType(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode());
                ChangeUserMoneyRes changeUserMoneyRes = userApi.changeUserMoney(changeUserMoneyReq);
                if (!StringUtils.equals(changeUserMoneyRes.getRetCode(), TradeEnums.RetEnum.SUCCESS.getCode())) {
                    throw new Exception("扣除用户余额失败！");
                }
            }
            //扣库存
            ReduceGoodsNumberReq reduceGoodsNumberReq = new ReduceGoodsNumberReq();
            reduceGoodsNumberReq.setOrderId(orderId);
            reduceGoodsNumberReq.setGoodsId(confirmOrderReq.getGoodsId());
            reduceGoodsNumberReq.setGoodsNumber(confirmOrderReq.getGoodsNumber());
            ReduceGoodsNumberRes reduceGoodsNumberRes = goodsApi.reduceGoodsNumber(reduceGoodsNumberReq);
            if (!StringUtils.equals(reduceGoodsNumberRes.getRetCode(),TradeEnums.RetEnum.SUCCESS.getCode())){
                throw new Exception("扣库存失败！");
            }
            if (1 == 1) {
                throw new Exception("人工抛出异常");
            }
            //更改订单状态
            TradeOrder tradeOrder = new TradeOrder();
            tradeOrder.setOrderId(orderId);
            tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.CONFIRM.getStatusCode());
            tradeOrder.setConfirmTime(new Date());
            int i = tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
            if (i <= 0) {
                throw new Exception("更改订单状态失败");
            }
        }catch (Exception e){
            //发送MQ消息
            CancelOrderMQ cancelOrderMQ = new CancelOrderMQ();
            cancelOrderMQ.setOrderId(orderId);
            cancelOrderMQ.setUserId(confirmOrderReq.getUserId());
            cancelOrderMQ.setGoodsNumber(confirmOrderReq.getGoodsNumber());
            cancelOrderMQ.setGoodsId(confirmOrderReq.getGoodsId());
            cancelOrderMQ.setCouponId(confirmOrderReq.getCouponId());
            cancelOrderMQ.setUserMoney(confirmOrderReq.getMoneyPaid());
            try {
                SendResult sendResult = this.aceMQProducer.sendMessage(MQEnums.TopicEnum.ORDER_CANCELL, orderId, JSON.toJSONString(cancelOrderMQ));
                System.out.println(sendResult);
            }catch (AceMQException ace){
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    private String  saveNoConfirmOrder(ConfirmOrderReq confirmOrderReq) throws Exception {
        TradeOrder tradeOrder = new TradeOrder();
        String orderId = IDGenerator.generatorID();
        tradeOrder.setOrderId(orderId);
        tradeOrder.setUserId(confirmOrderReq.getUserId());
        tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.NO_CONFIRM.getStatusCode());
        tradeOrder.setPayStatus(TradeEnums.PayStatusEnum.NO_PAY.getStatusCode());
        tradeOrder.setShoppingStatus(TradeEnums.ShippingStatusEnum.NO_SHIP.getStatusCode());
        tradeOrder.setAddress(confirmOrderReq.getAddress());
        tradeOrder.setConsignee(confirmOrderReq.getConsignee());
        tradeOrder.setGoodsId(confirmOrderReq.getGoodsId());
        tradeOrder.setGoodsNumber(confirmOrderReq.getGoodsNumber());
        tradeOrder.setGoodsPrice(confirmOrderReq.getGoodsPrice());
        BigDecimal goodsAmount = confirmOrderReq.getGoodsPrice().
                multiply(new BigDecimal(confirmOrderReq.getGoodsNumber()));
        tradeOrder.setGoodsAmount(goodsAmount);
        BigDecimal shippingFee = calculateShippingFee(goodsAmount);
        if(confirmOrderReq.getShippingFee().compareTo(shippingFee) != 0){
            throw new Exception("快递费用不正确");
        }
        tradeOrder.setShippingFee(shippingFee);
        BigDecimal orderAmount = goodsAmount.add(shippingFee);
        if(orderAmount.compareTo(confirmOrderReq.getOrderAmount()) != 0){
            throw new Exception("订单总价异常，请重新 下单！");
        }
        tradeOrder.setOrderAmount(orderAmount);
        String couponId = confirmOrderReq.getCouponId();
        //优惠券不为空
        if(StringUtils.isNoneBlank(couponId)){
            //查询优惠券状态
            QueryCouponReq queryCouponReq = new QueryCouponReq();
            queryCouponReq.setCouponId(couponId);
            QueryCouponRes queryCouponRes = couponApi.queryCoupon(queryCouponReq);
            if(queryCouponRes == null){
                throw new Exception("优惠券非法");
            }else if (!queryCouponRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS.getCode())){
                throw new Exception("优惠券非法");
            }
            if (!StringUtils.equals(queryCouponRes.getIsUsed(),TradeEnums.YesNoEnum.NO.getCode())){
                throw new Exception("优惠券已使用");
            }
            tradeOrder.setCouponId(couponId);
            tradeOrder.setCouponPaid(queryCouponRes.getCouponPrice());
        }else{
            tradeOrder.setCouponPaid(BigDecimal.ZERO);
        }
        //余额支付
        if(confirmOrderReq.getMoneyPaid() != null){
            int r = confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO);
            if (r == -1){
                throw new Exception("余额金额非法");
            }
            if (r == 1){
                //判断当时账户余额是否足够
                QueryUserReq queryUserReq = new QueryUserReq();
                queryUserReq.setUserId(confirmOrderReq.getUserId());
                QueryUserRes queryUserRes = userApi.queryUserById(queryUserReq);
                if(queryUserReq == null){
                    throw new Exception("用户非法");
                } else if (!StringUtils.equals(queryUserRes.getRetCode(), TradeEnums.RetEnum.SUCCESS.getCode())) {
                    throw new Exception("用户非法");
                }

                if (queryUserRes.getUserMoney().compareTo(confirmOrderReq.getMoneyPaid()) == -1){
                    throw new Exception("余额不足");
                }
                tradeOrder.setMoneyPaid(confirmOrderReq.getMoneyPaid());
            }
        }else {
            tradeOrder.setMoneyPaid(BigDecimal.ZERO);
        }

        BigDecimal payAount = orderAmount.subtract(tradeOrder.getMoneyPaid()).subtract(tradeOrder.getCouponPaid());

        tradeOrder.setPayAmount(payAount);
        tradeOrder.setAddTime(new Date());

        int ret = this.tradeOrderMapper.insert(tradeOrder);
        if (ret != 1){
            throw new Exception("保存订单失败");
        }
        return orderId;
    }

    private BigDecimal calculateShippingFee(BigDecimal goodsAmount) {
        if (goodsAmount.doubleValue() > 100.00){
            return BigDecimal.ZERO;
        }else{
            return new BigDecimal("10");
        }
    }

    private void checkConfirmOrderReq(ConfirmOrderReq confirmOrderReq,QueryGoodsRes queryGoodsRes) {
        if(confirmOrderReq == null){
            throw new AceOrderException("下单信息不能为空");
        }

        if(confirmOrderReq.getUserId() == null){
            throw new AceOrderException("会员账号不能为空");
        }

        if(confirmOrderReq.getGoodsId() == null){
            throw new AceOrderException("商品编号不能为空");
        }

        if (confirmOrderReq.getGoodsNumber() == null){
            throw new AceOrderException("购买数量不能为空");
        }else if (confirmOrderReq.getGoodsNumber() <= 0){
            throw new AceOrderException("购买数量不能小于0");
        }

        if(confirmOrderReq.getAddress() == null){
            throw new AceOrderException("收货地址不能为空");
        }

        if(queryGoodsRes == null
                ||
                !StringUtils.equals(queryGoodsRes.getRetCode(), TradeEnums.RetEnum.SUCCESS.getCode())){
            throw new AceOrderException("未查询到该商品[" + confirmOrderReq.getGoodsId() + "]");
        }

        if (queryGoodsRes.getGoodsNumber() < confirmOrderReq.getGoodsNumber()){
            throw new AceOrderException("商品库存不足");
        }

        if (queryGoodsRes.getGoodsPrice().compareTo(confirmOrderReq.getGoodsPrice()) != 0){
            throw new AceOrderException("当前商品价格有变化，请重新下单");
        }

        if(confirmOrderReq.getShippingFee() == null){
            confirmOrderReq.setShippingFee(BigDecimal.ZERO);
        }

        if (confirmOrderReq.getOrderAmount() == null) {
            confirmOrderReq.setOrderAmount(BigDecimal.ZERO);
        }
    }
}
