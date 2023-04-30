package com.jiahe.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;
import com.jiahe.service.OrderService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询所有订单
     * @param page
     * @param size
     * @param desc
     * @return
     */
    @GetMapping("/selectAllOrder")
    public Result selectAllOrder(@RequestParam Integer page, @RequestParam Integer size, Integer desc){
        IPage<OrderDto> data = orderService.selectAllOrder(page, size, desc);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"查询失败");
    }

    /**
     * 根据id查询订单详情
     * @param id
     */
    @GetMapping("/selectOrderDetailById")
    public Result selectOrderDetail(@RequestParam Integer id){
        OrderDto data = orderService.selectOrderDetail(id);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"订单不存在");
    }

    /**
     * 根据用户名查询订单
     * @param userName
     * @param desc
     * @return
     */
    @GetMapping("/selectOrderByUserName")
    public Result selectOrderByUserName(@RequestParam String userName, @RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer desc){
        IPage<OrderDto> data = orderService.selectOrderByUserName(userName, page, size, desc);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"查询失败");
    }

    /**
     * 根据id删除订单项
     * @param id
     */
    @PostMapping("/deleteOrderCommodity")
    public Result deleteOrderCommodity(@RequestParam Integer id){
        Boolean flag = orderService.deleteOrderCommodity(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 根据id删除订单
     * @param id
     */
    @PostMapping("/deleteOrder")
    public Result deleteOrder(@RequestParam Integer id){
        Boolean flag = orderService.deleteOrder(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 根据id修改订单项
     * 使用Json的方式传递参数
     * @param orderCommodity
     */
    @PostMapping("/updateOrderCommodity")
    public Result updateOrderCommodity(@RequestBody OrderCommodity orderCommodity){
        Boolean flag = orderService.updateOrderCommodity(orderCommodity);
        return new Result(null,flag? Code.UPDATE_SUCCESS:Code.UPDATE_FAIL,flag?"修改成功":"修改失败");
    }

    /**
     * 根据id修改订单
     * 使用Json的方式传递参数
     * @param order
     */
    @PostMapping("/updateOrder")
    public Result updateOrder(@RequestBody Order order){
        Boolean flag = orderService.updateOrder(order);
        return new Result(null,flag? Code.UPDATE_SUCCESS:Code.UPDATE_FAIL,flag?"修改成功":"修改失败");
    }

    /**
     * 新增多条订单项
     * 使用Json的方式传递参数，订单项对象数组
     * @param orderCommodities
     */
    @PostMapping("/insertOrderCommodity")
    public Result insertOrderCommodity(@RequestBody OrderCommodity[] orderCommodities, @RequestParam Integer userId){
        Boolean flag = orderService.insertOrderCommodities(userId, orderCommodities);
        return new Result(userId,flag? Code.ADD_SUCCESS:Code.ADD_FAIL,flag?"添加成功":"添加失败");
    }

    /**
     * 新增订单
     * 使用Json的方式传递参数
     * @param order
     */
    @PostMapping("/insertOrder")
    public Result insertOrder(@RequestBody Order order){
        Boolean flag = orderService.insertOrder(order);
        return new Result(order.getId(),flag? Code.ADD_SUCCESS:Code.ADD_FAIL,flag?"添加成功":"添加失败");
    }
}
