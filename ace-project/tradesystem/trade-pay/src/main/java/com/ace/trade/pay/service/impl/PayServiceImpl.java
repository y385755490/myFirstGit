package com.ace.trade.pay.service.impl;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.pay.CallbackPaymentReq;
import com.ace.trade.common.protocol.pay.CallbackPaymentRes;
import com.ace.trade.common.protocol.pay.CreatePaymentReq;
import com.ace.trade.common.protocol.pay.CreatePaymentRes;
import com.ace.trade.common.rocketmq.AceMQProducer;
import com.ace.trade.common.util.IDGenerator;
import com.ace.trade.entity.TradePay;
import com.ace.trade.entity.TradePayExample;
import com.ace.trade.mapper.TradePayMapper;
import com.ace.trade.pay.service.IPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    private TradePayMapper tradePayMapper;
    @Autowired
    private AceMQProducer aceMQProducer;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq) {
        CreatePaymentRes createPaymentRes = new CreatePaymentRes();
        createPaymentRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        createPaymentRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try {
            TradePayExample payExample = new TradePayExample();
            payExample.createCriteria().andOrderIdEqualTo(createPaymentReq.getOrderId()).
                    andIsPaidEqualTo(TradeEnums.YesNoEnum.YES.getCode());
            long count = this.tradePayMapper.countByExample(payExample);
            if (count > 0) {
                throw new Exception("该订单已支付");
            }
            String payId = IDGenerator.generatorID();
            TradePay tradePay = new TradePay();
            tradePay.setPayId(payId);
            tradePay.setOrderId(createPaymentReq.getOrderId());
            tradePay.setIsPaid(TradeEnums.YesNoEnum.NO.getCode());
            tradePay.setPayAmount(createPaymentReq.getPayAmount());
            tradePayMapper.insert(tradePay);
            System.out.println("创建支付订单成功：" + payId);
        } catch (Exception e) {
            createPaymentRes.setRetInfo(TradeEnums.RetEnum.FAIL.getDesc());
            createPaymentRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
        }
        return createPaymentRes;
    }

    @Transactional
    public CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq) {
        CallbackPaymentRes callbackPaymentRes = new CallbackPaymentRes();
        callbackPaymentRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        callbackPaymentRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        if (StringUtils.equals(callbackPaymentReq.getIsPaid(),TradeEnums.YesNoEnum.YES.getCode())){
            //更新支付状态
            TradePay tradePay = tradePayMapper.selectByPrimaryKey(callbackPaymentReq.getPayId());
            if (tradePay == null) {
                throw new RuntimeException("未找到支付单");
            }
        }
        return callbackPaymentRes;
    }
}
