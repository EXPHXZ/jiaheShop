package com.jiahe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.pojo.Aftermarket;
import com.jiahe.service.AftermarketService;
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

    /**
     * 获取所有的售后信息
     * @return
     */
    @GetMapping("/getAll")
    public Result getAll(){
        //获取没有删除的订单列表
        List<Aftermarket> list = aftermarketService.list(
                new LambdaQueryWrapper<Aftermarket>().eq(Aftermarket::getIsDeleted,0));
        return new Result(Code.SELECT_SUCCESS,list);
    }


    /**
     * 添加售后信息
     * @param aftermarket
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Aftermarket aftermarket){
        aftermarket.setIsDeleted(0);
        boolean save = aftermarketService.save(aftermarket);
        if (save){
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        }
        return new Result(null,Code.ADD_FAIL,"添加失败");
    }

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

    /**
     * 删除售后信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteCommodity(@PathVariable Integer id){
        Boolean flag = aftermarketService.removeById(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

}
