package com.ace.trade.user.service;

import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;

public interface IUserService {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
}