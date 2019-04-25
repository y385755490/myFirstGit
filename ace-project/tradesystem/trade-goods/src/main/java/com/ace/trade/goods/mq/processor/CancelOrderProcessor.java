package com.ace.trade.goods.mq.processor;

import com.ace.trade.common.constants.MQEnums;
import com.ace.trade.common.rocketmq.IMessageProcessor;
import com.ace.trade.entity.TradeMqConsumerLog;
import com.ace.trade.entity.TradeMqConsumerLogKey;
import com.ace.trade.goods.service.IGoodsService;
import com.ace.trade.mapper.TradeMqConsumerLogMapper;
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

            TradeMqConsumerLog mqConsumerLog = this.tradeMqConsumerLogMapper.selectByPrimaryKey(key);
            if (mqConsumerLog != null) {

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
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
