package com.itheima.stock.service;

import com.itheima.stock.pojo.domain.InnerMarketDomain;
import com.itheima.stock.vo.resp.R;

import java.util.List;

/**
 * ClassName: StockService
 * Package: com.itheima.stock.service
 * Description:股票服务接口
 *
 * @Author starry
 * @Create 2024/3/1 18:12
 * @Version 1.0
 */
public interface StockService {
    R<List<InnerMarketDomain>> innerIndexAll();
}
