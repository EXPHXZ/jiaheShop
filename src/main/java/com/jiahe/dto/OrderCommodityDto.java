package com.jiahe.dto;

import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.OrderCommodity;
import lombok.Data;

import java.util.List;

@Data
public class OrderCommodityDto extends OrderCommodity {

    private String commodityName;
    private String brandName;
    private String size;
    private String image;

    private Commodity commodity;

    @Override
    public String toString() {
        return super.toString()+"OrderCommodityDto{" +
                "commodityName='" + commodityName + '\'' +
                '}';
    }
}
