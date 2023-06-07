package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.dto.OrderDto;
import com.jiahe.dto.ShoppingCartDto;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;
import com.jiahe.pojo.ShoppingCart;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService extends IService<Order> {

    /**
     * 将商品加入购物车
     * @param commodityId
     * @param userId
     * @param count
     * @return
     */
    public Boolean addShoppingCart(Integer commodityId, Integer userId, Integer count);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    public List<ShoppingCartDto> selectShoppingCart(Integer userId);

    /**
     * 查询所有订单
     * @return
     */
    public IPage<OrderDto> selectAllOrder(Integer page, Integer size, Integer desc);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     */
    public OrderDto selectOrderDetail(Integer orderId);

    /**
     * 根据用户名查询订单
     * @param userName
     * @return
     */
    public IPage<OrderDto> selectOrderByUserName(String userName, Integer page, Integer size, Integer desc);

    /**
     * 新增订单
     * @param orderCommodities
     * @return
     */
    public Boolean insertOrderCommodities(Integer userId, Integer addressId, OrderCommodity[] orderCommodities);

    /**
     * 删除订单
     * @param id
     * @return
     */
    public Boolean deleteOrder(Integer id);

    /**
     * 进行退货操作
     * @param orderCommodity
     * @return
     */
    public Boolean updateOrderCommodity(@RequestParam Integer orderId, OrderCommodity orderCommodity);


    public Order searchOrder(Integer orderId);

    public Boolean updateOrderForAftermarket(Integer orderId);

    public Boolean updateOrderDetailForAftermarket(Integer orderId);

}
