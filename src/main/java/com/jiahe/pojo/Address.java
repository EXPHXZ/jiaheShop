package com.jiahe.pojo;

import lombok.Data;

@Data
public class Address {
    private Integer id;
    private Integer userId;
    private String contacts;
    private String contactsPhone;
    private String location;
    private String tag;
    private Integer isDefault;
    private Integer isDeleted;
}
