package com.ace.trade.entity;

import java.io.Serializable;

/**
 * trade_mq_consumer_log
 * @author 
 */
public class TradeMqConsumerLogKey implements Serializable {
    /**
     * 消费组名
     */
    private String groupName;

    /**
     * 消息TAG
     */
    private String msgTag;

    /**
     * 业务ID
     */
    private String msgKeys;

    private static final long serialVersionUID = 1L;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(String msgTag) {
        this.msgTag = msgTag;
    }

    public String getMsgKeys() {
        return msgKeys;
    }

    public void setMsgKeys(String msgKeys) {
        this.msgKeys = msgKeys;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TradeMqConsumerLogKey other = (TradeMqConsumerLogKey) that;
        return (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getMsgTag() == null ? other.getMsgTag() == null : this.getMsgTag().equals(other.getMsgTag()))
            && (this.getMsgKeys() == null ? other.getMsgKeys() == null : this.getMsgKeys().equals(other.getMsgKeys()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getMsgTag() == null) ? 0 : getMsgTag().hashCode());
        result = prime * result + ((getMsgKeys() == null) ? 0 : getMsgKeys().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupName=").append(groupName);
        sb.append(", msgTag=").append(msgTag);
        sb.append(", msgKeys=").append(msgKeys);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}