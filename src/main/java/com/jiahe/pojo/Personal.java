package com.jiahe.pojo;

import lombok.Data;

@Data
public class Personal {
    private Integer id;
    private String name;
    private Integer userId;
    private String sex;
    private String phoneNumber;
    private String email;
    private int isDeleted;
}
