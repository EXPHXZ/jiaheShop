package com.jiahe.controller;

import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result checkUser(@RequestBody User user){
        User user1 = userService.checkUser(user);
        return new Result(user1,user1 != null?Code.LOGIN_SUCCESS:Code.LOGIN_FAIL,user1 != null?"登录成功":"登录失败，请检查用户名和密码");
    }

    @PostMapping
    public Result addUser(@RequestBody User user) throws Exception {
        if (userService.addUser(user))
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        else
            return new Result(null,Code.ADD_FAIL,"添加失败,相同的角色已经存在于数据库中");
    }

    @DeleteMapping("/{user}")
    public Result deleteUser(@RequestBody User user) throws Exception{
        if (userService.deleteUser(user))
            return new Result(null,Code.ADD_SUCCESS,"删除成功");
        else
            return new Result(null,Code.ADD_FAIL,"删除失败,删除的角色不存在于数据库中");
    }

    @PutMapping
    public Result updateUser(@RequestBody User user) throws Exception{
        if (userService.updateUser(user))
            return new Result(null,Code.ADD_SUCCESS,"修改成功");
        else
            return new Result(null,Code.ADD_FAIL,"修改失败,修改的角色不存在于数据库中");
    }
}
