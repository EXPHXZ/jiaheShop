package com.jiahe.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer identity;
    @TableLogic(value = "0",delval = "1")
    private int isDeleted;
}
