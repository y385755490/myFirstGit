package com.ace.trade.common.rocketmq;

import com.ace.trade.common.exception.AceMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AceMQConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(AceMQConsumer.class);

    private DefaultMQPushConsumer consumer;
    private String groupName;
    private String nameSrvAddr;
    private String topic;
    private String tag = "*";
    private int consumeThreadMin = 20;
    private int consumeThreadMax = 64;

    private IMessageProcessor MessageProcessor;

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public void setMessageProcessor(IMessageProcessor messageProcessor) {
        MessageProcessor = messageProcessor;
    }

    public void init() throws AceMQException {
        if(StringUtils.isBlank(this.groupName)){
            throw new AceMQException("groupName is blank!");
        }
        if(StringUtils.isBlank(this.nameSrvAddr)){
            throw new AceMQException("nameSrvAddr is blank!");
        }
        this.consumer = new DefaultMQPushConsumer(this.groupName);
        this.consumer.setNamesrvAddr(this.nameSrvAddr);
        try {
            this.consumer.subscribe(this.topic, this.tag);
        } catch (MQClientException e) {

        }
        this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //wrong time format 2017_0422_221800
        this.consumer.setConsumeTimestamp("20181109221800");
        this.consumer.setConsumeThreadMax(this.consumeThreadMax);
        this.consumer.setConsumeThreadMin(this.consumeThreadMin);
        this.consumer.setVipChannelEnabled(false);
        AceMessageListener aceMessageListener = new AceMessageListener();
        aceMessageListener.setMessageProcessor(this.MessageProcessor);
        this.consumer.registerMessageListener(aceMessageListener);
        try {
            this.consumer.start();
            LOGGER.info("consumer is start!groupName:{},topic:{},nameAddr:{}",this.groupName,this.topic,
                    this.nameSrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("consumer is error!groupName:{},topic:{},nameAddr:{}",this.groupName,this.topic,
                    this.nameSrvAddr);
            throw new AceMQException(e);
        }
        System.out.printf("Consumer Started.%n");

    }
}
