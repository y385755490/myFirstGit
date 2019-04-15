package com.ace.trade.order.service.impl;

import com.ace.trade.common.api.IGoodsApi;
import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.exception.AceOrderException;
import com.ace.trade.common.protocol.goods.QueryGoodsReq;
import com.ace.trade.common.protocol.goods.QueryGoodsRes;
import com.ace.trade.common.protocol.order.ConfirmOrderReq;
import com.ace.trade.common.protocol.order.ConfirmOrderRes;
import com.ace.trade.common.util.IDGenerator;
import com.ace.trade.entity.TradeOrder;
import com.ace.trade.mapper.TradeOrderMapper;
import com.ace.trade.order.service.IOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IGoodsApi goodsApi;

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq) {
        ConfirmOrderRes confirmOrderRes = new ConfirmOrderRes();
        try{
            QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
            queryGoodsReq.setGoodsId(confirmOrderReq.getGoodsId());
            QueryGoodsRes queryGoodsRes = goodsApi.queryGoods(queryGoodsReq);
            //1.检查校验
            checkConfirmOrderReq(confirmOrderReq,queryGoodsRes);
            //2.创建不可见订单
            saveNoConfirmOrder(confirmOrderReq);
        }catch (Exception e){
            confirmOrderRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            confirmOrderRes.setRetInfo(e.getMessage());
        }
        //3.调用远程服务，扣优惠券、扣库存、扣余额，如果调用成功->更改订单状态可见，失败->发送MQ消除，进行取消订单

        return confirmOrderRes;
    }

    private void saveNoConfirmOrder(ConfirmOrderReq confirmOrderReq) throws Exception {
        TradeOrder tradeOrder = new TradeOrder();
        String orderId = IDGenerator.generatorID();
        tradeOrder.setOrderId(orderId);
        tradeOrder.setUserId(confirmOrderReq.getUserId());
        tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.CONFIRM.getStatusCode());


        int ret = this.tradeOrderMapper.insert(tradeOrder);
        if (ret != 1){
            throw new Exception("保存不可见订单失败");
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
