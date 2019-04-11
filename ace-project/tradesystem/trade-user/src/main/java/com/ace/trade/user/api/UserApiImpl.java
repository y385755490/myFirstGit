package com.ace.trade.user.api;

import com.ace.trade.common.api.IUserApi;
import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;
import com.ace.trade.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class UserApiImpl implements IUserApi {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/queryUserById",method = RequestMethod.POST)
    @ResponseBody
    public QueryUserRes queryUserById(@RequestBody QueryUserReq queryUserReq) {
        return this.userService.queryUserById(queryUserReq);
    }

}