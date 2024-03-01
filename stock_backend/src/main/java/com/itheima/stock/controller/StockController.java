package com.itheima.stock.controller;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
