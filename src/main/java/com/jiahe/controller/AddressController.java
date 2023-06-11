package com.jiahe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Admins;
import com.jiahe.pojo.Users;
import com.jiahe.service.AddressService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usersAddress")
public class AddressController {

    @Autowired
    private AddressService addressService;

//    分页查询
    @GetMapping("/{id}/{current}/{size}")
    public Result selectByPage(@PathVariable Integer id, @PathVariable Integer current, @PathVariable Integer size) {
        IPage<Address> page = addressService.selectByPage(id,current,size);
        return new Result(Code.SELECT_SUCCESS,page);
    }
//    查询全部地址
    @GetMapping("/searchPersonalAll/{id}")
    public Result searchAll(@PathVariable Integer id){
        if(addressService.searchPersonalAll(id) != null)
            return new Result(addressService.searchPersonalAll(id),Code.SELECT_SUCCESS,"查询成功");
        else
            return new Result(null,Code.SELECT_FAIL,"查询失败");
    }
//    用户增加个人地址
    @PostMapping("/addPersonalAddress")
    public Result addPersonalAddress(@RequestBody Address address){
        Boolean flag = addressService.addPersonalAddress(address);
        if(flag)
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        else
            return new Result(null,Code.ADD_FAIL,"添加失败");
    }
//    搜索要修改的回显地址信息
    @GetMapping("/searchUpdateAddress/{id}")
    public Result searchUpdateAddress(@PathVariable Integer id){
            return new Result(Code.SELECT_SUCCESS,addressService.searchUpdateAddress(id));
    }
//    修改地址
    @PutMapping("/updatePersonalAddress")
    public Result updatePersonalAddress(@RequestBody Address address){
        Boolean flag = addressService.updatePersonalAddress(address);
        if(flag)
            return new Result(null,Code.UPDATE_SUCCESS,"修改成功");
        else
            return new Result(null,Code.UPDATE_FAIL,"修改失败");
    }
//    删除地址
    @DeleteMapping("/deletePersonalAddress/{id}")
    public Result deletePersonalAddress(@PathVariable Integer id){
        Boolean flag = addressService.deletePersonalAddress(id);
        if(flag)
            return new Result(null,Code.DELETE_SUCCESS,"删除成功");
        else
            return new Result(null,Code.DELETE_FAIL,"删除失败");
    }
}
