package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;

import java.util.List;

public interface CommodityService {

    public IPage<Commodity> SelectAll(int current, int pageSize);

    public Boolean addCommodity(Commodity commodity);

    public Boolean deleteCommodity(Integer id);

    public Commodity searchCommodity(Integer id);

    public Boolean updateCommodity(Commodity commodity);

    public List<Commodity> searchCommodity(Commodity commodity);

    public Boolean deleteCommodities(List<Commodity> commodities);
}
