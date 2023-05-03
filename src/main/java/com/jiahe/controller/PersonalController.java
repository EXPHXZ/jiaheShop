package com.jiahe.controller;

import com.jiahe.pojo.Personal;
import com.jiahe.service.PersonalService;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

//    @PostMapping
//    public Result addPersonalInfo(@RequestBody Personal personal) {
//        Boolean
//    }

    @GetMapping("/{uid}")
    public String searchPersonalInfo(@PathVariable Integer uid) {
        System.out.println(personalService.searchPersonalInfoByUid(uid).toString());
        return "1";
    }

//    @DeleteMapping

}
