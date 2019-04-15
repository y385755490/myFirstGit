package com.ace.trade.order.api;

import com.ace.trade.common.api.IOrderApi;
import com.ace.trade.common.protocol.order.ConfirmOrderReq;
import com.ace.trade.common.protocol.order.ConfirmOrderRes;
import com.ace.trade.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrderApiImpl implements IOrderApi {
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/confirmOrder",method = RequestMethod.POST)
    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq) {
        ConfirmOrderRes confirmOrderRes = this.orderService.confirmOrder(confirmOrderReq);
        return confirmOrderRes;
    }
}
