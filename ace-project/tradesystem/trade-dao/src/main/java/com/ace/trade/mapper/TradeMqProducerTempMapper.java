package com.ace.trade.mapper;

import com.ace.trade.entity.TradeMqProducerTemp;
import com.ace.trade.entity.TradeMqProducerTempExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeMqProducerTempMapper {
    long countByExample(TradeMqProducerTempExample example);

    int deleteByExample(TradeMqProducerTempExample example);

    int deleteByPrimaryKey(String id);

    int insert(TradeMqProducerTemp record);

    int insertSelective(TradeMqProducerTemp record);

    List<TradeMqProducerTemp> selectByExample(TradeMqProducerTempExample example);

    TradeMqProducerTemp selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TradeMqProducerTemp record, @Param("example") TradeMqProducerTempExample example);

    int updateByExample(@Param("record") TradeMqProducerTemp record, @Param("example") TradeMqProducerTempExample example);

    int updateByPrimaryKeySelective(TradeMqProducerTemp record);

    int updateByPrimaryKey(TradeMqProducerTemp record);
}