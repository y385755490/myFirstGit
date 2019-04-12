package com.ace.trade.common.api;

import com.ace.trade.common.protocol.user.ChangeUserMoneyReq;
import com.ace.trade.common.protocol.user.ChangeUserMoneyRes;
import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;

public interface IUserApi {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq);
}
