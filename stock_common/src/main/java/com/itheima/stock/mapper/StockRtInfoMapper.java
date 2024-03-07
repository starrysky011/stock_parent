package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.Stock4EvrDayDomain;
import com.itheima.stock.pojo.domain.Stock4MinuteDomain;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author starrysky
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-02-22 16:31:17
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查询指定时间范围内每分钟涨停或者跌停的数量
     *  @param openTime 开始时间
     *  @param curTime 结束时间 一般开始时间和结束时间在同一天
     *  @param flag 约定:1->涨停 0:->跌停
     *  @return
     */
    List<Map> getStockUpdownCount(@Param("openTime") Date openTime, @Param("curTime") Date curTime, @Param("flag") int flag);

    /**
     * 统计指定时间点下，各个涨跌区间内股票的个数
     * @param avlDate
     * @return
     */
    List<Map> getStockUpDownSectionByTime(@Param("avlDate") Date avlDate);

    /**
     * 根据时间范围查询指定股票的交易流水
     * @param code 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4MinuteDomain> getStockInfoByCodeAndDate(@Param("code") String code,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    /**
     * 查询指定日期范围内指定股票每天的交易数据
     * @param stockCode 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4EvrDayDomain> getStockInfo4EvrDay(@Param("stockCode") String stockCode,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);

    /**
     * 获取指定日期范围内的收盘日期
     * @param code 股票编码
     * @param beginDate 起始时间
     * @param endDate 结束时间
     * @return
     */
    List<Date> getCloseDates(@Param("code") String code,
                             @Param("beginDate") Date beginDate,
                             @Param("endDate") Date endDate);

    /**
     * 获取指定股票在指定日期点下的数据
     * @param code 股票编码
     * @param dates 指定日期集合
     * @return
     */
    List<Stock4EvrDayDomain> getStockCreenDkLineData(@Param("code") String code,
                                                     @Param("dates") List<Date> dates);



    /**
     * 批量插入功能
     * @param stockRtInfoList
     */
    int insertBatch(List<StockRtInfo> stockRtInfoList);
}
