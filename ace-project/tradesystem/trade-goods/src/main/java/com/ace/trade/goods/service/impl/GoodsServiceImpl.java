package com.ace.trade.goods.service.impl;

import com.ace.trade.common.constants.TradeEnums;
import com.ace.trade.common.protocol.goods.*;
import com.ace.trade.entity.TradeGoods;
import com.ace.trade.entity.TradeGoodsNumberLog;
import com.ace.trade.entity.TradeGoodsNumberLogKey;
import com.ace.trade.goods.service.IGoodsService;
import com.ace.trade.mapper.TradeGoodsMapper;
import com.ace.trade.mapper.TradeGoodsNumberLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private TradeGoodsMapper tradeGoodsMapper;
    @Autowired
    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;
    public static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    public QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq) {
        QueryGoodsRes queryGoodsRes = new QueryGoodsRes();
        queryGoodsRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        queryGoodsRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try {
            if (queryGoodsReq == null){
                throw new Exception("查询商品信息ID不正确");
            }else if (queryGoodsReq.getGoodsId() == null){
                throw new Exception("查询商品信息ID不正确");
            }
            TradeGoods tradeGoods = this.tradeGoodsMapper.selectByPrimaryKey(queryGoodsReq.getGoodsId());
            if (tradeGoods != null) {
                BeanUtils.copyProperties(tradeGoods,queryGoodsRes);
            }else {
                throw new Exception("未查询到商品");
            }
        } catch (Exception e) {
            queryGoodsRes.setRetInfo(TradeEnums.RetEnum.FAIL.getDesc());
            queryGoodsRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            LOGGER.error(e.getMessage(),e);
        }
        return queryGoodsRes;
    }

    @Transactional
    public ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq) {
        ReduceGoodsNumberRes reduceGoodsNumberRes = new ReduceGoodsNumberRes();
        reduceGoodsNumberRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        reduceGoodsNumberRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        if (reduceGoodsNumberReq == null){
            throw new RuntimeException("扣减库存请求参数不正确");
        }else if (reduceGoodsNumberReq.getGoodsId() == null || reduceGoodsNumberReq.getGoodsNumber() <= 0){
            throw new RuntimeException("扣减库存请求参数不正确");
        }
        TradeGoods tradeGoods = new TradeGoods();
        tradeGoods.setGoodsId(reduceGoodsNumberReq.getGoodsId());
        tradeGoods.setGoodsNumber(reduceGoodsNumberReq.getGoodsNumber());
        int i = this.tradeGoodsMapper.reduceGoodsNumber(tradeGoods);
        if (i <= 0){
            throw new RuntimeException("扣减库存失败");
        }
        TradeGoodsNumberLog tradeGoodsNumberLog = new TradeGoodsNumberLog();
        tradeGoodsNumberLog.setGoodsId(reduceGoodsNumberReq.getGoodsId());
        tradeGoodsNumberLog.setGoodsNumber(reduceGoodsNumberReq.getGoodsNumber());
        tradeGoodsNumberLog.setOrderId(reduceGoodsNumberReq.getOrderId());
        tradeGoodsNumberLog.setLogTime(new Date());
        this.tradeGoodsNumberLogMapper.insert(tradeGoodsNumberLog);
        return reduceGoodsNumberRes;
    }

    public AddGoodsNumberRes addGoodsNumber(AddGoodsNumberReq addGoodsNumberReq) {
        AddGoodsNumberRes addGoodsNumberRes = new AddGoodsNumberRes();
        addGoodsNumberRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        addGoodsNumberRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try {
            if (addGoodsNumberReq == null){
                throw new RuntimeException("增加库存请求参数不正确");
            }else if (addGoodsNumberReq.getGoodsId() == null || addGoodsNumberReq.getGoodsNumber() <= 0){
                throw new RuntimeException("增加库存请求参数不正确");
            }
            if (addGoodsNumberReq.getGoodsId() != null){
                TradeGoodsNumberLogKey key = new TradeGoodsNumberLogKey();
                key.setGoodsId(addGoodsNumberReq.getGoodsId());
                key.setOrderId(addGoodsNumberReq.getOrderId());
                TradeGoodsNumberLog tradeGoodsNumberLog = this.tradeGoodsNumberLogMapper.selectByPrimaryKey(key);
                if (tradeGoodsNumberLog == null) {
                    throw new Exception("未找到扣库存记录");
                }
            }
            TradeGoods tradeGoods = new TradeGoods();
            tradeGoods.setGoodsId(addGoodsNumberReq.getGoodsId());
            tradeGoods.setGoodsNumber(addGoodsNumberReq.getGoodsNumber());
            int i = this.tradeGoodsMapper.addGoodsNumber(tradeGoods);
            if (i <= 0) {
                throw new Exception("增加库存失败");
            }
        }catch (Exception e){
            addGoodsNumberRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            addGoodsNumberRes.setRetInfo(TradeEnums.RetEnum.FAIL.getDesc());
            LOGGER.error(e.getMessage(),e);
        }

        return addGoodsNumberRes;
    }
}
