package com.ace.trade.goods.mq.processor;

import com.ace.trade.common.constants.MQEnums;
import com.ace.trade.common.protocol.goods.AddGoodsNumberReq;
import com.ace.trade.common.protocol.mq.CancelOrderMQ;
import com.ace.trade.common.rocketmq.IMessageProcessor;
import com.ace.trade.entity.TradeMqConsumerLog;
import com.ace.trade.entity.TradeMqConsumerLogExample;
import com.ace.trade.entity.TradeMqConsumerLogKey;
import com.ace.trade.goods.service.IGoodsService;
import com.ace.trade.mapper.TradeMqConsumerLogMapper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelOrderProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private TradeMqConsumerLogMapper tradeMqConsumerLogMapper;

    public boolean handleMessage(MessageExt messageExt) {
        TradeMqConsumerLog mqConsumerLog = null;
        try {
            String groupName = "goods_orderTopic_cancel_group";
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:" + messageExt);

            TradeMqConsumerLogKey key = new TradeMqConsumerLogKey();
            key.setGroupName(groupName);
            key.setMsgTag(tags);
            key.setMsgKeys(keys);

            mqConsumerLog = this.tradeMqConsumerLogMapper.selectByPrimaryKey(key);
            if (mqConsumerLog != null) {
                String consumerStatus = mqConsumerLog.getConsumerStatus();
                if (StringUtils.equals(MQEnums.ConsumerStatusEnum.SUCCESS.getStatusCode(),consumerStatus)){
                    //返回成功，重复的处理消息
                    LOGGER.warn("已经处理过，不用再处理了");
                    return true;
                }else if (StringUtils.equals(MQEnums.ConsumerStatusEnum.PROCESSING.getStatusCode(),consumerStatus)){
                    //返回失败，说明有消费者正在处理当中，稍后再试
                    LOGGER.warn("正在处理，稍后再试");
                    return false;
                }else if (StringUtils.equals(MQEnums.ConsumerStatusEnum.FAIL.getStatusCode(),consumerStatus)){
                    if (mqConsumerLog.getConsumerTimes() >= 3){
                        //让这个消息不再重试，转人工处理
                        LOGGER.warn("超过3次，不再处理");
                        return true;
                    }
                    //更新处理中状态
                    TradeMqConsumerLog updateMqConsumerLog = new TradeMqConsumerLog();
                    updateMqConsumerLog.setGroupName(groupName);
                    updateMqConsumerLog.setMsgTag(tags);
                    updateMqConsumerLog.setMsgKeys(keys);
                    updateMqConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.SUCCESS.getStatusCode());
                    //防止并发
                    TradeMqConsumerLogExample example = new TradeMqConsumerLogExample();
                    example.createCriteria().andGroupNameEqualTo(mqConsumerLog.getGroupName()).
                            andMsgKeysEqualTo(mqConsumerLog.getMsgKeys()).andMsgTagEqualTo(mqConsumerLog.getMsgTag()).
                            andConsumerTimesEqualTo(mqConsumerLog.getConsumerTimes());
                    //乐观锁的方式防止并发更新
                    int i = this.tradeMqConsumerLogMapper.updateByExampleSelective(updateMqConsumerLog,example);
                    if (i <= 0){
                        LOGGER.warn("并发更新处理状态，一会儿重试");
                        return false;
                    }
                }
            }else {
                //新插入去重表，并发时用主键冲突控制
                try {
                    TradeMqConsumerLog tradeMqConsumerLog = new TradeMqConsumerLog();
                    tradeMqConsumerLog.setGroupName(groupName);
                    tradeMqConsumerLog.setMsgKeys(keys);
                    tradeMqConsumerLog.setMsgTag(tags);
                    tradeMqConsumerLog.setMsgId(msgId);
                    tradeMqConsumerLog.setConsumerTimes(0);
                    tradeMqConsumerLog.setMsgBody(body);
                    tradeMqConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.PROCESSING.getStatusCode());
                    this.tradeMqConsumerLogMapper.insertSelective(tradeMqConsumerLog);
                }catch (Exception e){
                    LOGGER.warn("主键冲突，说明有订阅者正在处理，请稍后再试");
                    return false;
                }
            }
            //业务逻辑处理
            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            AddGoodsNumberReq addGoodsNumberReq = new AddGoodsNumberReq();
            addGoodsNumberReq.setGoodsId(cancelOrderMQ.getGoodsId());
            addGoodsNumberReq.setGoodsNumber(cancelOrderMQ.getGoodsNumber());
            addGoodsNumberReq.setOrderId(cancelOrderMQ.getOrderId());
            goodsService.addGoodsNumber(addGoodsNumberReq);
            //更新消息处理成功
            TradeMqConsumerLog updateMqConsumerLog = new TradeMqConsumerLog();
            updateMqConsumerLog.setGroupName(groupName);
            updateMqConsumerLog.setMsgTag(tags);
            updateMqConsumerLog.setMsgKeys(keys);
            updateMqConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.SUCCESS.getStatusCode());
            updateMqConsumerLog.setConsumerTimes(1);
            this.tradeMqConsumerLogMapper.updateByPrimaryKeySelective(updateMqConsumerLog);
            return true;
        } catch (Exception e) {
            //更新消息处理失败
            TradeMqConsumerLog updateMqConsumerLog = new TradeMqConsumerLog();
            updateMqConsumerLog.setGroupName(mqConsumerLog.getGroupName());
            updateMqConsumerLog.setMsgTag(mqConsumerLog.getMsgTag());
            updateMqConsumerLog.setMsgKeys(mqConsumerLog.getMsgKeys());
            updateMqConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.FAIL.getStatusCode());
            updateMqConsumerLog.setConsumerTimes(mqConsumerLog == null ? 1 : mqConsumerLog.getConsumerTimes() + 1);
            this.tradeMqConsumerLogMapper.updateByPrimaryKeySelective(updateMqConsumerLog);
            return false;
        }
    }
}
