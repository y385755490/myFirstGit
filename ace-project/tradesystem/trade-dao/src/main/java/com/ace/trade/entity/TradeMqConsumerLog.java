package com.ace.trade.entity;

import java.io.Serializable;

/**
 * trade_mq_consumer_log
 * @author 
 */
public class TradeMqConsumerLog extends TradeMqConsumerLogKey implements Serializable {
    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息内容
     */
    private String msgBody;

    /**
     * 消息状态 0-正在处理 1-处理成功 2-处理失败
     */
    private String consumerStatus;

    /**
     * 消费次数
     */
    private Integer consumerTimes;

    /**
     * 备注（错误原因）
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getConsumerStatus() {
        return consumerStatus;
    }

    public void setConsumerStatus(String consumerStatus) {
        this.consumerStatus = consumerStatus;
    }

    public Integer getConsumerTimes() {
        return consumerTimes;
    }

    public void setConsumerTimes(Integer consumerTimes) {
        this.consumerTimes = consumerTimes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        TradeMqConsumerLog other = (TradeMqConsumerLog) that;
        return (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getMsgTag() == null ? other.getMsgTag() == null : this.getMsgTag().equals(other.getMsgTag()))
            && (this.getMsgKeys() == null ? other.getMsgKeys() == null : this.getMsgKeys().equals(other.getMsgKeys()))
            && (this.getMsgId() == null ? other.getMsgId() == null : this.getMsgId().equals(other.getMsgId()))
            && (this.getMsgBody() == null ? other.getMsgBody() == null : this.getMsgBody().equals(other.getMsgBody()))
            && (this.getConsumerStatus() == null ? other.getConsumerStatus() == null : this.getConsumerStatus().equals(other.getConsumerStatus()))
            && (this.getConsumerTimes() == null ? other.getConsumerTimes() == null : this.getConsumerTimes().equals(other.getConsumerTimes()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getMsgTag() == null) ? 0 : getMsgTag().hashCode());
        result = prime * result + ((getMsgKeys() == null) ? 0 : getMsgKeys().hashCode());
        result = prime * result + ((getMsgId() == null) ? 0 : getMsgId().hashCode());
        result = prime * result + ((getMsgBody() == null) ? 0 : getMsgBody().hashCode());
        result = prime * result + ((getConsumerStatus() == null) ? 0 : getConsumerStatus().hashCode());
        result = prime * result + ((getConsumerTimes() == null) ? 0 : getConsumerTimes().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", msgId=").append(msgId);
        sb.append(", msgBody=").append(msgBody);
        sb.append(", consumerStatus=").append(consumerStatus);
        sb.append(", consumerTimes=").append(consumerTimes);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}