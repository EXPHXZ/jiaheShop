package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.CommodityDao;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityDao commodityDao;

    @Override
    public IPage<Commodity> SelectAll(int current, int pageSize) {
        IPage<Commodity> page = new Page<>(current,pageSize);
        IPage<Commodity> page1 = commodityDao.selectPage(page,null);
        return page1;
    }

    @Override
    public Boolean addCommodity(Commodity commodity) {
        return commodityDao.insert(commodity) > 0;
    }

    @Override
    public Boolean deleteCommodity(Integer id) {
        return commodityDao.deleteById(id) > 0;
    }

    @Override
    public Commodity searchCommodity(Integer id) {
        return commodityDao.selectById(id);
    }

    @Override
    public Boolean updateCommodity(Commodity commodity) {
        return commodityDao.updateById(commodity) > 0;
    }


    @Override
    public List<Commodity> searchCommodity(Commodity commodity) {
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(commodity.getCommodityName() != null,Commodity::getCommodityName,commodity.getCommodityName());
        wrapper.like(commodity.getCategory() != null,Commodity::getCategory,commodity.getCategory());
        wrapper.like(commodity.getStatus() != null,Commodity::getStatus,commodity.getStatus());
        List<Commodity> commodities = commodityDao.selectList(wrapper);
        return commodities;
    }

    @Override
    public Boolean deleteCommodities(List<Commodity> commodities) {
        List<Integer> ids = new ArrayList<>();
        for(Commodity commodity : commodities){
            ids.add(commodity.getId());
        }
        return commodityDao.deleteBatchIds(ids) > 0;
    }


//    判断添加的商品的名字和规格都相同的商品是否已经存在于数据库中
    @Override
    public Boolean checkAdd(Commodity commodity) {
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commodity::getCommodityName,commodity.getCommodityName());
        wrapper.eq(Commodity::getSize,commodity.getSize());
        Commodity commodity1 = commodityDao.selectOne(wrapper);
        return commodity1 != null;
    }
}
