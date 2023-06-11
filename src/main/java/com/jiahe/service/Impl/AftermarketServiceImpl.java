package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.dao.AftermarketDao;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Aftermarket;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.service.AftermarketService;
import com.jiahe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author  
 * @since 2023-04-29
 */

@Service
public class AftermarketServiceImpl extends ServiceImpl<AftermarketDao, Aftermarket> implements AftermarketService {

    @Autowired
    private AftermarketService aftermarketService;

    @Autowired
    private OrderService orderService;

    @Transactional
    @Override
    public Boolean add(Aftermarket aftermarket){
            Boolean flag = aftermarketService.save(aftermarket);
            Integer orderId = aftermarket.getOrderId();
            Integer orderCommodityId = aftermarket.getOrderCommodityId();
            //把订单下面需要退货的订单项的状态改成1退货中
            Boolean flag2 = orderService.updateOrderDetailForAftermarket(orderCommodityId,1);
            //把订单状态改成4-退货中
            Boolean flag1 = orderService.updateOrderForAftermarket(orderId,4);
            return flag&&flag1&&flag2;

    }

    @Transactional
    @Override
    public Boolean returnAftermarket(Aftermarket aftermarket) {
        //获取售后id
        Integer id = aftermarket.getId();
        //获取订单id
        Integer orderId = aftermarket.getOrderId();
        //获取订单项id
        Integer orderCommodityId = aftermarket.getOrderCommodityId();
        //处理退货时，先修改订单项的订单状态，然后再修改订单的状态

        //把订单下面需要退货的订单项的状态改成2已受理
        Boolean flag2 = orderService.updateOrderDetailForAftermarket(orderCommodityId,2);


        //把订单状态改成5-已经处理
        Boolean flag1 = orderService.updateOrderForAftermarket(orderId,5);

        //把售后信息的状态也改一下
        aftermarket.setStatus(1);

        Boolean flag = aftermarketService.updateById(aftermarket);

        return flag&&flag1&&flag2;
    }
}

