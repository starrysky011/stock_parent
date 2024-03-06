package com.itheima.stock.controller;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ClassName: StockController
 * Package: com.itheima.stock.controller
 * Description:定义股票相关接口控制器
 *
 * @Author starry
 * @Create 2024/3/1 18:03
 * @Version 1.0
 */
@Api(value = "/api/quot", tags = {"ClassName: StockController Package: com.itheima.stock.controller Description:定义股票相关接口控制器"})
@RestController
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;

    /**
     * 获取国内最新大盘指数
     * @return
     */
    @ApiOperation(value = "获取国内最新大盘指数", notes = "获取国内最新大盘指数", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> innerIndexAll(){

        return stockService.innerIndexAll();
    }

    /**
     * 需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    @ApiOperation(value = "获取沪深两市板块最新数据", notes = "需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询
     * @param page
     * @param pageSize
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "")
    })
    @ApiOperation(value = "分页查询股票最新数据，并按照涨幅排序查询", notes = "分页查询股票最新数据，并按照涨幅排序查询", httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult> getStockPageInfo(
            @RequestParam(name = "page", required = false,defaultValue = "1")Integer page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize){
        return stockService.getStockPageInfo(page,pageSize);
    }

    /**
     * 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     * @return
     */


    @ApiOperation(value = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", notes = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", httpMethod = "GET")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockInfo(){
        return stockService.getStockInfo();
    }


    /**
     * 统计最新交易日下股票每分钟涨跌停的数量
     * @return
     */
    @ApiOperation(value = "统计最新交易日下股票每分钟涨跌停的数量", notes = "统计最新交易日下股票每分钟涨跌停的数量", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }

    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "将指定页的股票数据导出到excel表下", notes = "将指定页的股票数据导出到excel表下", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void stockExport(@RequestParam(name = "page", required = false,defaultValue = "1")Integer page,
                            @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize, HttpServletResponse response){
        stockService.stockExport(page,pageSize,response);
    }

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    @ApiOperation(value = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", notes = "功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map> stockTradeVol4InnerMarket(){
        return stockService.stockTradeVol4InnerMarket();
    }

    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    @ApiOperation(value = "查询当前时间下股票的涨跌幅度区间统计功能 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点", notes = "查询当前时间下股票的涨跌幅度区间统计功能 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点", httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map> getStockUpDown(){
        return stockService.stockUpDownScopeCount();
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "功能描述：查询单个个股的分时行情数据", notes = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点", httpMethod = "GET")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(@RequestParam(value = "code" ,required = true) String code){
        return stockService.stockScreenTimeSharing(code);
    }


    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据", notes = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据", httpMethod = "GET")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getDayKLinData(@RequestParam("code") String stockCode){
        return stockService.stockCreenDkLine(stockCode);
    }
}
