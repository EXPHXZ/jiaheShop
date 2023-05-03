package com.jiahe.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CommodityService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    /**
     * 分页查询所有商品数据
     * @param current 当前页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/{current}/{pageSize}")
    public Result selectAll(@PathVariable int current, @PathVariable int pageSize){
        IPage<Commodity> page = commodityService.SelectAll(current,pageSize);
        //当删除到页数发生变化的时候
        if (current > page.getPages()){
            page = commodityService.SelectAll((int)page.getPages(),pageSize);
        }
        return new Result(Code.SELECT_SUCCESS,page);
    }

    /**
     * 根据传来的json数据来添加商品数据
     * @param commodity
     * @return
     */
    @PostMapping
    public Result addCommodity(@RequestBody Commodity commodity) throws Exception {
        Boolean addFlag = commodityService.checkAdd(commodity);
        if (addFlag == true){
            return new Result(null,Code.ADD_FAIL,"相同的商品已经存在于数据库中");
        }
        Boolean flag = commodityService.addCommodity(commodity);
        if(flag){
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        }else {
            return new Result(null,Code.ADD_FAIL,"添加失败");
        }

    }

    /**
     * 根据id删除商品数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteCommodity(@PathVariable Integer id){
        Boolean flag = commodityService.deleteCommodity(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 根据id查询商品数据来回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result searchCommodity(@PathVariable Integer id){
        Commodity commodity = commodityService.searchCommodity(id);
        return new Result(Code.SELECT_SUCCESS,commodity);
    }

    /**
     * 根据传来的json实体类来更新数据
     * @param commodity
     * @return
     */
    @PutMapping
    public Result updateCommodity(@RequestBody Commodity commodity){
        Boolean checkFlag = commodityService.checkAdd(commodity);
        if(checkFlag == true){
            return new Result(null,Code.UPDATE_FAIL,"修改失败，相同的商品已经存在");
        }
        Boolean flag = commodityService.updateCommodity(commodity);
        return new Result(null,flag?Code.UPDATE_SUCCESS:Code.UPDATE_FAIL,flag?"修改成功":"修改失败");
    }

    /**
     * 根据传入的json实体类来进行条件查询
     * @param commodity
     * @return
     */
    @PostMapping("/search")
    public Result search(@RequestBody Commodity commodity){
        List<Commodity> commodities = commodityService.searchCommodity(commodity);
        return new Result(commodities,Code.SELECT_SUCCESS,"共查询到"+commodities.size()+"条数据");
    }

    /**
     * 根据传来的Json数据列表来进行批量删除
     * @param commodities
     * @return
     */
    @PostMapping("/delete")
    public Result deleteCommodities(@RequestBody List<Commodity> commodities){
        Boolean flag = commodityService.deleteCommodities(commodities);
        return new Result(null,flag?Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"批量删除成功":"批量删除失败");
    }
}
