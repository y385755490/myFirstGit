package com.ace.trade.user.mq.processor;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.mq.CancelOrderMQ;
import com.ace.trade.common.protocol.user.ChangeUserMoneyReq;
import com.ace.trade.common.rocketmq.IMessageProcessor;
import com.ace.trade.user.service.IUserService;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class CancelOrderProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private IUserService userService;

    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:" + messageExt);

            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            if (cancelOrderMQ.getUserMoney() != null && cancelOrderMQ.getUserMoney().compareTo(BigDecimal.ZERO) == 1){
                ChangeUserMoneyReq changeUserMoneyReq = new ChangeUserMoneyReq();
                changeUserMoneyReq.setUserId(cancelOrderMQ.getUserId());
                changeUserMoneyReq.setMoneyLogType(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode());
                changeUserMoneyReq.setOrderId(cancelOrderMQ.getOrderId());
                changeUserMoneyReq.setUserMoney(cancelOrderMQ.getUserMoney());
                userService.changeUserMoney(changeUserMoneyReq);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
