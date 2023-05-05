package com.jiahe.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.jiahe.pojo.Personal;
import com.jiahe.service.PersonalService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/{uid}")
    public Result searchPersonalInfo(@PathVariable Integer uid) {
        Personal personal = personalService.searchPersonalInfoByUid(uid);
        if (personal == null)
            return new Result(Code.SELECT_FAIL, personalService.searchPersonalInfoByUid(uid));
        else
            return new Result(Code.SELECT_SUCCESS, personalService.searchPersonalInfoByUid(uid));
    }

    @PostMapping
    public Result addPersonalInfo(@RequestBody Personal personal) {
        if (personalService.addPersonalInfo(personal))
            return new Result(1, Code.ADD_SUCCESS, "个人信息添加成功");
        else
            return new Result(0, Code.ADD_FAIL, "个人信息添加失败");
    }

    @DeleteMapping("/{uid}")
    public Result deletePersonalInfo(@PathVariable Integer uid) {
        if (personalService.deletePersonalInfoByUid(uid))
            return new Result(1, Code.DELETE_SUCCESS, "个人信息删除成功");
        else
            return new Result(0, Code.DELETE_FAIL, "个人信息删除失败");
    }

    @PutMapping
    public Result updatePersonalInfo(@RequestBody Personal personal) {
        if (personalService.updatePersonalInfo(personal))
            return new Result(1, Code.UPDATE_SUCCESS, "个人信息修改成功");
        else
            return new Result(0, Code.UPDATE_FAIL, "个人信息修改失败");
    }

}
