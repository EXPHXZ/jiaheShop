package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.dao.CommodityDao;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityDao,Commodity> implements CommodityService {

    @Autowired
    private CommodityDao commodityDao;

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
        wrapper.like(commodity.getBrandName() != null,Commodity::getBrandName,commodity.getBrandName());
        wrapper.eq(commodity.getCategoryId() != null,Commodity::getCategoryId,commodity.getCategoryId());
        wrapper.eq(commodity.getStatus() != null,Commodity::getStatus,commodity.getStatus());
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


//    判断添加的商品的名字和品牌名和规格都相同的商品是否已经存在于数据库中
    @Override
    public Boolean checkAdd(Commodity commodity) {
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Commodity::getCommodityName,commodity.getCommodityName());
        wrapper.eq(Commodity::getBrandName,commodity.getBrandName());
        wrapper.eq(Commodity::getSize,commodity.getSize());
        Commodity commodity1 = commodityDao.selectOne(wrapper);
        return commodity1 != null;
    }


    public List<Commodity> doSelect(List<Commodity> list){
        ArrayList<Commodity> list1 = list.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(
                                item -> item.getCommodityName() + ";" + item.getBrandName())
                        )), ArrayList::new
                ));

        return list1;

    }
}
