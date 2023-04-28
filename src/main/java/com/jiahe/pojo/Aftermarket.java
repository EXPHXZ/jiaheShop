package com.jiahe.pojo;
import lombok.Data;

@Data
public class Aftermarket {
    private Integer id;
    private Integer orderId;
    private String cause;
    private int isDeleted;
}
