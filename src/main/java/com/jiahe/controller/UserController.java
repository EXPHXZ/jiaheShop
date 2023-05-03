package com.jiahe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/userManagement")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result checkUser(@RequestBody User user, HttpServletRequest request){
        User user1 = userService.checkUser(user);
        if (user1 != null){
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
        }
        return new Result(user1,user1 != null?Code.LOGIN_SUCCESS:Code.LOGIN_FAIL,user1 != null?"登录成功":"登录失败，请检查用户名和密码");
    }

//  查询全部
    @GetMapping("/{current}/{size}")
    public Result selectAll(@RequestParam Integer current, @RequestParam Integer size){
        IPage<User> page = userService.selectAll(current,size);
        return new Result(Code.SELECT_SUCCESS,page);
    }
//  新增用户
    @PostMapping
    public Result addUser(@RequestBody User user) throws Exception {
        if (userService.addUser(user))
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        else
            return new Result(null,Code.ADD_FAIL,"添加失败,相同的角色已经存在于数据库中");
    }

//    根据用户id单条删除数据
    @DeleteMapping("delete/{id}")
    public Result deleteUser(@PathVariable Integer id) throws Exception{
        if (userService.deleteUser(id))
            return new Result(null,Code.ADD_SUCCESS,"删除成功");
        else
            return new Result(null,Code.ADD_FAIL,"删除失败,删除的角色不存在于数据库中");
    }

//    批量删除数据
    @DeleteMapping ("/delete")
    public Result deleteUsers(@RequestParam List<User> users){
        Boolean flag = userService.deleteUsers(users);
        return new Result(null,flag?Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"批量删除成功":"批量删除失败");
    }

//  修改用户信息
    @PutMapping("update")
    public Result updateUser(@RequestParam User user) throws Exception{
        if (userService.updateUser(user))
            return new Result(null,Code.ADD_SUCCESS,"修改成功");
        else
            return new Result(null,Code.ADD_FAIL,"修改失败,修改的角色不存在于数据库中");
    }

//   搜索表单点击查询之后执行的办法
    @GetMapping("/search")
    public Result searchUser(@RequestParam User user) throws Exception {
        List<User> users = userService.searchUser(user);
        return new Result(users,Code.SELECT_SUCCESS,"查询到"+users.size()+"条数据");
    }

//  根据用户id查找要修改的用户信息并回显到修改表上
    @GetMapping("/search/{id}")
    public Result searchUpdateUser(@PathVariable Integer id){
        return new Result(userService.searchUpdateUser(id),Code.SELECT_SUCCESS,"查询成功");
    }
}
