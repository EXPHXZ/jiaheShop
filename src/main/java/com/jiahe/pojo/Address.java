package com.jiahe.pojo;

import lombok.Data;

@Data
public class Address {
    private Integer id;
    private Integer user_id;
    private String contacts;
    private String contactsPhone;
    private String location;
    private Integer tag;
    private Integer isDefault;
    private Integer isDeleted;
}
