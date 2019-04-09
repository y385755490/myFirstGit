package com.ace.trade.common.client;

import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        QueryUserReq queryUserReq = new QueryUserReq();
        queryUserReq.setUserId(1);
        restTemplate.postForObject("http://localhost:8080/user/queryUserById",queryUserReq, QueryUserRes.class);
    }
}
