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

//    用户增加个人地址
    @PostMapping("/addPersonalAddress")
    public Result addPersonalAddress(@RequestBody Address address){
        Boolean flag = addressService.addPersonalAddress(address);
        if(flag)
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        else
            return new Result(null,Code.ADD_FAIL,"添加失败，已有设置为默认的地址");
    }

//    根据地址搜索要修改的回显信息
    @GetMapping("/searchUpdateAddress")
    public Result searchUpdateAddress(@RequestBody String location){
            return new Result(Code.SELECT_SUCCESS,addressService.searchUpdateAddress(location));
    }
}
