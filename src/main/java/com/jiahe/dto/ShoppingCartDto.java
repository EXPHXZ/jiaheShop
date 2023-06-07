package com.jiahe.dto;

import com.jiahe.pojo.Address;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.ShoppingCart;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoppingCartDto extends ShoppingCart {

    private String brandName;
    private String commodityName;
    private String image;
    private BigDecimal price;
    private BigDecimal discount;
    private String size;
    private Integer count;

    @Override
    public String toString() {
        return super.toString()+"ShoppingCartDto{" +
                "brandName='" + brandName + '\'' +
                ", commodityName='" + commodityName + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", size='" + size + '\'' +
                ", count=" + count +
                '}';
    }
}
