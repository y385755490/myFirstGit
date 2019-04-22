package com.ace.trade.common.protocol.user;

import com.ace.trade.common.protocol.BaseRes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class QueryUserRes extends BaseRes implements Serializable {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    private String userName;

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
}
