package com.ace.trade.common.client;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RestClient {
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {

        QueryUserReq queryUserReq = new QueryUserReq();
        queryUserReq.setUserId(1);

        QueryUserRes queryUserRes = restTemplate.postForObject(TradeEnums.RestServerEnum.USER.getServerUrl() +
                "queryUserById", queryUserReq, QueryUserRes.class);
        System.out.println(queryUserRes.getUserName());
    }

}
