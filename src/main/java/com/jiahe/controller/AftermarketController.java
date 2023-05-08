package com.jiahe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.pojo.Aftermarket;
import com.jiahe.pojo.Order;
import com.jiahe.service.AftermarketService;
import com.jiahe.service.OrderService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  
 * @since 2023-04-29
 */
@RestController
@RequestMapping("/aftermarket")
public class AftermarketController {

    @Autowired
    AftermarketService aftermarketService;

    @Autowired
    OrderService orderService;

    /**
     * 获取所有的售后信息
     * @return
     */

    @GetMapping("/getAll")
    public Result getAll(){
        //获取没有删除的订单列表
        List<Aftermarket> list = aftermarketService.list(
                new LambdaQueryWrapper<Aftermarket>().eq(Aftermarket::getIsDeleted,0));
        //遍历售后信息根据订单id获取到的对应的订单状态
        for(Aftermarket aftermarket:list){
            Integer orderId = aftermarket.getOrderId();
            //调用orderService写好的查询订单功能查询到对于的订单信息，拿到订单状态
            Order order = orderService.searchOrder(orderId);
            aftermarket.setStatus(order.getStatus());
        }

        return new Result(Code.SELECT_SUCCESS,list);
    }



    /*@PostMapping("/add")
    public Result add(@RequestBody Aftermarket aftermarket){
        aftermarket.setIsDeleted(0);
        boolean save = aftermarketService.save(aftermarket);
        if (save){
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        }
        return new Result(null,Code.ADD_FAIL,"添加失败");
    }*/

    /**
     * 修改售后信息
     * @param aftermarket
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Aftermarket aftermarket){
        boolean b = aftermarketService.updateById(aftermarket);
        if (b){
            return new Result(null,Code.UPDATE_SUCCESS,"修改成功");
        }
        return new Result(null,Code.UPDATE_FAIL,"修改失败");
    }






    @PostMapping("/delete")
    public Result handleReturnCommodity(@RequestBody Aftermarket aftermarket){
        Integer id = aftermarket.getId();
        Integer orderId = aftermarket.getOrderId();
        //处理退货时，先修改订单项的订单状态，然后再修改订单的状态
        Order order = orderService.searchOrder(orderId);
        //把订单下面需要退货的订单的状态改成2已退货
        Boolean flag2 = orderService.updateOrderDetailForAftermarket(orderId);
        //把订单状态改成0-正常订单
        Boolean flag1 = orderService.updateOrderForAftermarket(order);
        //删除订单
        Boolean flag = aftermarketService.removeById(id);
        return new Result(null,flag&&flag1&&flag2? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"处理退货成功":"处理退货失败");
    }

}
