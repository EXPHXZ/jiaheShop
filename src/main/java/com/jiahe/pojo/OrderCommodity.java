package com.jiahe.pojo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderCommodity {
    private Integer id;
    private Integer orderId;
    private Integer commodityId;
    private Integer count;
    private BigDecimal price;
    private BigDecimal priceSum;
    private Integer status;
}
