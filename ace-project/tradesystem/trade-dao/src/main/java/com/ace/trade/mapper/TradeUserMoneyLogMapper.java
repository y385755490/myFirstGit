package com.ace.trade.mapper;

import com.ace.trade.entity.TradeUserMoneyLog;
import com.ace.trade.entity.TradeUserMoneyLogExample;
import com.ace.trade.entity.TradeUserMoneyLogKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeUserMoneyLogMapper {
    long countByExample(TradeUserMoneyLogExample example);

    int deleteByExample(TradeUserMoneyLogExample example);

    int deleteByPrimaryKey(TradeUserMoneyLogKey key);

    int insert(TradeUserMoneyLog record);

    int insertSelective(TradeUserMoneyLog record);

    List<TradeUserMoneyLog> selectByExample(TradeUserMoneyLogExample example);

    TradeUserMoneyLog selectByPrimaryKey(TradeUserMoneyLogKey key);

    int updateByExampleSelective(@Param("record") TradeUserMoneyLog record, @Param("example") TradeUserMoneyLogExample example);

    int updateByExample(@Param("record") TradeUserMoneyLog record, @Param("example") TradeUserMoneyLogExample example);

    int updateByPrimaryKeySelective(TradeUserMoneyLog record);

    int updateByPrimaryKey(TradeUserMoneyLog record);
}