package com.ace.trade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * trade_coupon
 * @author 
 */
public class TradeCoupon implements Serializable {
    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 是否已使用 0未使用 1已使用
     */
    private String isUsed;

    /**
     * 使用时间
     */
    private Date usedTime;

    private static final long serialVersionUID = 1L;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
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
        TradeCoupon other = (TradeCoupon) that;
        return (this.getCouponId() == null ? other.getCouponId() == null : this.getCouponId().equals(other.getCouponId()))
            && (this.getCouponPrice() == null ? other.getCouponPrice() == null : this.getCouponPrice().equals(other.getCouponPrice()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getIsUsed() == null ? other.getIsUsed() == null : this.getIsUsed().equals(other.getIsUsed()))
            && (this.getUsedTime() == null ? other.getUsedTime() == null : this.getUsedTime().equals(other.getUsedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCouponId() == null) ? 0 : getCouponId().hashCode());
        result = prime * result + ((getCouponPrice() == null) ? 0 : getCouponPrice().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getIsUsed() == null) ? 0 : getIsUsed().hashCode());
        result = prime * result + ((getUsedTime() == null) ? 0 : getUsedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", couponId=").append(couponId);
        sb.append(", couponPrice=").append(couponPrice);
        sb.append(", userId=").append(userId);
        sb.append(", orderId=").append(orderId);
        sb.append(", isUsed=").append(isUsed);
        sb.append(", usedTime=").append(usedTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}