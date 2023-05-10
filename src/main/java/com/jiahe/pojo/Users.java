package com.jiahe.pojo;

import lombok.Data;

@Data
public class Users {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String sex;
    private String phone;
    private String avatar;
    private int isDeleted;
}
