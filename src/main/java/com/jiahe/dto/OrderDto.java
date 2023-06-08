package com.jiahe.dto;

import com.jiahe.pojo.Address;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.OrderCommodity;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Order {

    private String username;

    private Address address;

    private List<OrderCommodityDto> orderCommodityList;

    @Override
    public String toString() {
        return super.toString()+"OrderDto{" +
                "username='" + username + '\'' +
                ", orderCommodityList=" + orderCommodityList +
                ", address="+address +
                '}';
    }
}
