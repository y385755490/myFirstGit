import com.ace.trade.common.api.*;
import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.coupon.QueryCouponReq;
import com.ace.trade.common.protocol.coupon.QueryCouponRes;
import com.ace.trade.common.protocol.goods.QueryGoodsReq;
import com.ace.trade.common.protocol.goods.QueryGoodsRes;
import com.ace.trade.common.protocol.order.ConfirmOrderReq;
import com.ace.trade.common.protocol.order.ConfirmOrderRes;
import com.ace.trade.common.protocol.pay.CallbackPaymentReq;
import com.ace.trade.common.protocol.pay.CallbackPaymentRes;
import com.ace.trade.common.protocol.pay.CreatePaymentReq;
import com.ace.trade.common.protocol.pay.CreatePaymentRes;
import com.ace.trade.common.protocol.user.QueryUserReq;
import com.ace.trade.common.protocol.user.QueryUserRes;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@ContextConfiguration(locations = {"classpath:xml/spring-rest-client.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestTrade {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IGoodsApi goodsApi;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ICouponApi couponApi;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IUserApi userApi;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IOrderApi orderApi;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IPayApi payApi;

    @Test
    public void testUser(){
        QueryUserReq queryUserReq = new QueryUserReq();
        queryUserReq.setUserId(1);
        QueryUserRes queryUserRes = userApi.queryUserById(queryUserReq);
        System.out.println(JSON.toJSONString(queryUserRes));
    }

    @Test
    public void testGoods(){
        QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
        queryGoodsReq.setGoodsId(10000);
        QueryGoodsRes queryGoodsRes = goodsApi.queryGoods(queryGoodsReq);
        System.out.println(JSON.toJSONString(queryGoodsRes));
    }

    @Test
    public void testCoupon(){
        QueryCouponReq queryCouponReq = new QueryCouponReq();
        queryCouponReq.setCouponId("123456789");
        QueryCouponRes queryCouponRes = couponApi.queryCoupon(queryCouponReq);
        System.out.println(JSON.toJSONString(queryCouponRes));
    }

    @Test
    public void testConfirmOrder(){
        ConfirmOrderReq confirmOrderReq = new ConfirmOrderReq();
        confirmOrderReq.setGoodsId(10000);
        confirmOrderReq.setUserId(1);
        confirmOrderReq.setGoodsNumber(1);
        confirmOrderReq.setAddress("北京");
        confirmOrderReq.setGoodsPrice(new BigDecimal("5000"));
        confirmOrderReq.setOrderAmount(new BigDecimal("5000"));
        confirmOrderReq.setMoneyPaid(new BigDecimal("100"));
        confirmOrderReq.setCouponId("123456789");
        ConfirmOrderRes confirmOrderRes = orderApi.confirmOrder(confirmOrderReq);
        System.out.println(JSON.toJSONString(confirmOrderRes));
    }

    @Test
    public void testCreatePayment(){
        CreatePaymentReq createPaymentReq = new CreatePaymentReq();
        createPaymentReq.setPayAmount(new BigDecimal("4800"));
        createPaymentReq.setOrderId("2402a5f40fd547e9bcd92efe090b6fa5");
        CreatePaymentRes createPaymentRes = payApi.createPayment(createPaymentReq);
        System.out.println(JSON.toJSONString(createPaymentRes));
    }

    @Test
    public void testCallbackPayment(){
        CallbackPaymentReq callbackPaymentReq = new CallbackPaymentReq();
        callbackPaymentReq.setPayId("3a74d63949b34ab9957b49e9b6d88b3e");
        callbackPaymentReq.setIsPaid(TradeEnums.YesNoEnum.YES.getCode());
        CallbackPaymentRes callbackPaymentRes = payApi.callbackPayment(callbackPaymentReq);
        System.out.println(JSON.toJSONString(callbackPaymentRes));
    }
}
