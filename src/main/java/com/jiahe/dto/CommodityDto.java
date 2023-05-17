package com.jiahe.dto;

import com.jiahe.pojo.Commodity;
import lombok.Data;

@Data
public class CommodityDto extends Commodity {

    private String categoryName;

    @Override
    public String toString() {
        return super.toString()+"CommodityDto{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }
}
