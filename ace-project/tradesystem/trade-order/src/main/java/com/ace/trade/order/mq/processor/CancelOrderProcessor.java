package com.ace.trade.order.mq.processor;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.mq.CancelOrderMQ;
import com.ace.trade.common.rocketmq.IMessageProcessor;
import com.ace.trade.entity.TradeOrder;
import com.ace.trade.mapper.TradeOrderMapper;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelOrderProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:" + messageExt);
            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            TradeOrder record = new TradeOrder();
            record.setOrderId(cancelOrderMQ.getOrderId());
            record.setOrderStatus(TradeEnums.OrderStatusEnum.CANCEL.getStatusCode());
            tradeOrderMapper.updateByPrimaryKeySelective(record);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
