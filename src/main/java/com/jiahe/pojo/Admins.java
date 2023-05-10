package com.jiahe.pojo;

import lombok.Data;

@Data
public class Admins {
    private Integer id;
    private String account;
    private String password;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private Integer identity;
    private int isDeleted;
}
