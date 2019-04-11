package com.ace.trade.common.client;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.user.QueryUserRes;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RestClientProxyFactoryBeanSpring implements FactoryBean {
    private RestTemplate restTemplate = new RestTemplate();
    private Class serviceInterface;
    private TradeEnums.RestServerEnum serverEnum;

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServerEnum(TradeEnums.RestServerEnum serverEnum) {
        this.serverEnum = serverEnum;
    }

    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{serviceInterface},new ClientProxy());
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }

    private class ClientProxy implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return restTemplate.postForObject(TradeEnums.RestServerEnum.USER.getServerUrl() + method.getName(),
                    args[0],method.getReturnType());
        }
    }
}