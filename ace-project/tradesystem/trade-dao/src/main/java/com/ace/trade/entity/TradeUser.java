package com.ace.trade.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * trade_user
 * @author 
 */
public class TradeUser implements Serializable {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 积分
     */
    private Integer userScore;

    /**
     * 注册时间
     */
    private Date userRegTime;

    /**
     * 用户余额
     */
    private BigDecimal userMoney;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Date getUserRegTime() {
        return userRegTime;
    }

    public void setUserRegTime(Date userRegTime) {
        this.userRegTime = userRegTime;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
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
        TradeUser other = (TradeUser) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserPassword() == null ? other.getUserPassword() == null : this.getUserPassword().equals(other.getUserPassword()))
            && (this.getUserMobile() == null ? other.getUserMobile() == null : this.getUserMobile().equals(other.getUserMobile()))
            && (this.getUserScore() == null ? other.getUserScore() == null : this.getUserScore().equals(other.getUserScore()))
            && (this.getUserRegTime() == null ? other.getUserRegTime() == null : this.getUserRegTime().equals(other.getUserRegTime()))
            && (this.getUserMoney() == null ? other.getUserMoney() == null : this.getUserMoney().equals(other.getUserMoney()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserPassword() == null) ? 0 : getUserPassword().hashCode());
        result = prime * result + ((getUserMobile() == null) ? 0 : getUserMobile().hashCode());
        result = prime * result + ((getUserScore() == null) ? 0 : getUserScore().hashCode());
        result = prime * result + ((getUserRegTime() == null) ? 0 : getUserRegTime().hashCode());
        result = prime * result + ((getUserMoney() == null) ? 0 : getUserMoney().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userPassword=").append(userPassword);
        sb.append(", userMobile=").append(userMobile);
        sb.append(", userScore=").append(userScore);
        sb.append(", userRegTime=").append(userRegTime);
        sb.append(", userMoney=").append(userMoney);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}