package com.ace.trade.common.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;

public interface IMessageProcessor {
    /**
     * 处理消息
     * @param messageExt
     * @return
     */
    public boolean handleMessage(MessageExt messageExt);
}
