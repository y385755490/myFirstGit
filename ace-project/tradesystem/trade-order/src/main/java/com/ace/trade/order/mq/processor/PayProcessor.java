package com.ace.trade.order.mq.processor;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.mq.PaidMQ;
import com.ace.trade.common.rocketmq.IMessageProcessor;
import com.ace.trade.entity.TradeOrder;
import com.ace.trade.mapper.TradeOrderMapper;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PayProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(PayProcessor.class);
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:" + messageExt);

            PaidMQ paidMQ = JSON.parseObject(body, PaidMQ.class);

            TradeOrder record = new TradeOrder();
            record.setOrderId(paidMQ.getOrderId());
            record.setPayStatus(TradeEnums.PayStatusEnum.PAID.getStatusCode());
            tradeOrderMapper.updateByPrimaryKeySelective(record);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
