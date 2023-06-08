package com.jiahe.pojo;

import lombok.Data;

@Data
public class ShoppingCart {
    private Integer id;
    private Integer commodityId;
    private Integer userId;
    private Integer count;
    private Integer isDeleted;

}
