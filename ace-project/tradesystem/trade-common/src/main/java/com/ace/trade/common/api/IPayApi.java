package com.ace.trade.common.api;

import com.ace.trade.common.protocol.pay.CallbackPaymentReq;
import com.ace.trade.common.protocol.pay.CallbackPaymentRes;
import com.ace.trade.common.protocol.pay.CreatePaymentReq;
import com.ace.trade.common.protocol.pay.CreatePaymentRes;

public interface IPayApi {
    public CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq);
    public CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq);
}
