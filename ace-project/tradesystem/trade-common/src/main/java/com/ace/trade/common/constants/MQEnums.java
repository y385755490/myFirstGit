package com.ace.trade.common.constants;

public class MQEnums {
    public enum TopicEnum{
        ORDER_CONFIRM("orderTopic","confirm"),ORDER_CANCELL("orderTopic","cancel"),PAY_PAID("payTopic","paid");

        private String topic;
        private String tag;

        TopicEnum(String topic, String tag) {
            this.topic = topic;
            this.tag = tag;
        }

        public String getTopic() {
            return topic;
        }

        public String getTag() {
            return tag;
        }
    }
}
