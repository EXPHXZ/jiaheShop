package com.jiahe.dto;

import com.jiahe.pojo.OrderCommodity;
import lombok.Data;

import java.util.List;

@Data
public class OrderCommodityDto extends OrderCommodity {

    private String commodityName;

    @Override
    public String toString() {
        return super.toString()+"OrderCommodityDto{" +
                "commodityName='" + commodityName + '\'' +
                '}';
    }
}
