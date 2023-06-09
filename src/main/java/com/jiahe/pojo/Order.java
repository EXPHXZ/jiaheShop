package com.jiahe.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@TableName("orders")
public class Order {
    private Integer id;
    private Integer userId;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer count;
    private Integer sum;
    private Integer status;
    private Integer addressId;
    private Timestamp submitTime;
    private Integer isDeleted;
}
