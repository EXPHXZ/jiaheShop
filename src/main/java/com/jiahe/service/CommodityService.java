package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.Order;

import java.util.List;

public interface CommodityService extends IService<Commodity> {

    /**
     * 添加商品数据
     * @param commodity
     * @return
     */
    public Boolean addCommodity(Commodity commodity);


    /**
     * 根据id删除商品数据
     * @param id
     * @return
     */
    public Boolean deleteCommodity(Integer id);

    /**
     * 根据id来进行查询商品数据
     * @param id
     * @return
     */
    public Commodity searchCommodity(Integer id);

    /**
     * 更新商品数据
     * @param commodity
     * @return
     */
    public Boolean updateCommodity(Commodity commodity);

    /**
     * 根据传来的条件来进行条件查询
     * @param commodity
     * @return
     */
    public List<Commodity> searchCommodity(Commodity commodity);

    /**
     * 批量删除
     * @param commodities
     * @return
     */
    public Boolean deleteCommodities(List<Commodity> commodities);

    public Boolean checkAdd(Commodity commodity);

    public List<Commodity> doSelect(List<Commodity> commodities);

    public Boolean checkUpdate(Commodity commodity);

}
