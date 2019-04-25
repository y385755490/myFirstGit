package com.ace.trade.goods.service;

import com.ace.trade.common.protocol.goods.*;

public interface IGoodsService {
    public QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq);
    public ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq);
    public AddGoodsNumberRes addGoodsNumber(AddGoodsNumberReq addGoodsNumberReq);
}
