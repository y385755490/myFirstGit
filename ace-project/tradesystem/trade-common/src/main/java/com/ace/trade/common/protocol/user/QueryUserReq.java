package com.ace.trade.common.protocol.user;

import java.io.Serializable;

public class QueryUserReq implements Serializable {
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
