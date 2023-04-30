package com.jiahe.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Order {
    private Integer id;
    private String userId;
    private String status;
    private Timestamp submitTime;
    private BigDecimal price;
    private Integer count;
    private Integer sum;
    @TableField(exist = false)
    private List<OrderCommodity> orderCommodities;
}
