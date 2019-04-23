package com.ace.trade.coupon.service.impl;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.ace.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.ace.trade.common.protocol.coupon.QueryCouponReq;
import com.ace.trade.common.protocol.coupon.QueryCouponRes;
import com.ace.trade.coupon.service.ICouponService;
import com.ace.trade.entity.TradeCoupon;
import com.ace.trade.mapper.TradeCouponMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements ICouponService {
    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq) {
        QueryCouponRes queryCouponRes = new QueryCouponRes();
        queryCouponRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        queryCouponRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try {
            if (queryCouponReq == null){
                throw new Exception("请求参数不正确，优惠券编号为空");
            }else if (StringUtils.isBlank(queryCouponReq.getCouponId())){
                throw new Exception("请求参数不正确，优惠券编号为空");
            }
            TradeCoupon tradeCoupon = this.tradeCouponMapper.selectByPrimaryKey(queryCouponReq.getCouponId());
            if (tradeCoupon != null) {
                BeanUtils.copyProperties(tradeCoupon,queryCouponRes);
            }else {
                throw new Exception("未查询到该优惠券");
            }
        }catch (Exception e){
            queryCouponRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            queryCouponRes.setRetInfo(TradeEnums.RetEnum.FAIL.getDesc());
        }
        return null;
    }

    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq) {
        ChangeCouponStatusRes changeCouponStatusRes = new ChangeCouponStatusRes();
        changeCouponStatusRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        changeCouponStatusRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try {
            if (changeCouponStatusReq == null){
                throw new Exception("请求参数不正确，优惠券编号为空");
            }else if (StringUtils.isBlank(changeCouponStatusReq.getCouponId())){
                throw new Exception("请求参数不正确，优惠券编号为空");
            }
            //使用优惠券
            if (StringUtils.equals(changeCouponStatusReq.getIsUsed(),TradeEnums.YesNoEnum.YES.getCode())){

            }else if (StringUtils.equals(changeCouponStatusReq.getIsUsed(),TradeEnums.YesNoEnum.NO.getCode())){

            }
        }catch (Exception e){

        }
        return null;
    }
}
