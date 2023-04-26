package com.jiahe.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Commodity {
    private Integer id;
    private String commodityName;
    private String simpleDescription;
    private String detailedDescription;
    private String size;
    private String category;
    private BigDecimal price;
    private Integer status;
    private Integer remnant;
    private int isDeleted;

}
