package com.jiahe.pojo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品实体类
 */

@Data
public class Commodity {
    private Integer id;
    private String commodityName;
    private String image;
    private String brandName;
    private String simpleDescription;
    private String detailedDescription;
    private String size;
    private int categoryId;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer status;
    private Integer remnant;
    private int isDeleted;
}
