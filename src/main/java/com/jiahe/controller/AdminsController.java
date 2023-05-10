package com.jiahe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Admins;
import com.jiahe.pojo.Users;
import com.jiahe.service.AdminsService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/adminsManagement")
public class AdminsController {

    @Autowired
    private AdminsService adminsService;

    @PostMapping("/login")
    public Result checkAdmins(@RequestBody Admins admins, HttpServletRequest request) {
        Admins checkAdmin = adminsService.checkAdmins(admins);
        if (checkAdmin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", admins);
            return new Result(checkAdmin, Code.LOGIN_SUCCESS, "登录成功");
        }
        else
            return new Result(null, Code.LOGIN_FAIL, "登录失败，请检查账号和密码");
    }

    @GetMapping("/{current}/{size}")
    public Result selectByPage(@PathVariable Integer current, @PathVariable Integer size) {
        IPage<Admins> page = adminsService.selectByPage(current, size);
        return new Result(Code.SELECT_SUCCESS, page);
    }

    @PostMapping("/addAdmin")
    public Result addAdmins(@RequestBody Admins admins) {
        if (adminsService.addAdmins(admins))
            return new Result(1, Code.ADD_SUCCESS, "添加成功");
        else
            return new Result(0, Code.ADD_FAIL, "添加失败，相同的用户信息已经存在于数据库中");
    }

    @PostMapping("/deleteBatchAdmins")
    public Result deleteBatchAdmins(@RequestBody List<Admins> admins) {
        if (adminsService.deleteBatchAdmins(admins))
            return new Result(1, Code.DELETE_SUCCESS, "批量删除成功");
        else
            return new Result(0, Code.DELETE_FAIL, "批量删除失败");
    }

    @DeleteMapping("delete/{id}")
    public Result deleteAdmins(@PathVariable Integer id) {
        if (adminsService.deleteAdmins(id))
            return new Result(1, Code.DELETE_SUCCESS, "删除成功");
        else
            return new Result(0, Code.DELETE_FAIL, "删除失败，删除的角色不存在于数据库中");
    }

    @PutMapping
    public Result updateAdmins(@RequestBody Admins admins) {
        if (adminsService.updateAdmins(admins))
            return new Result(1, Code.UPDATE_SUCCESS, "修改成功");
        else
            return new Result(0, Code.UPDATE_FAIL, "修改失败，相同的用户信息已存在于数据库中");
    }

    @PostMapping("/search")
    public Result searchAdmins(@RequestBody Admins admin) {
        List<Admins> admins = adminsService.searchAdmins(admin);
        return new Result(admins, Code.SELECT_SUCCESS, "查询成功");
    }

    @GetMapping("/search/{id}")
    public Result searchUpdateAdmins(@PathVariable Integer id) {
        return new Result(adminsService.searchUpdateAdmins(id), Code.SELECT_SUCCESS,"查询成功");
    }
}
