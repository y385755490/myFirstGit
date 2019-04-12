package com.ace.trade.common.api;

import com.ace.trade.common.protocol.order.ConfirmOrderReq;
import com.ace.trade.common.protocol.order.ConfirmOrderRes;

public interface IOrderApi {
    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq);
}
