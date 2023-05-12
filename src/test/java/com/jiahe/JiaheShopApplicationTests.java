package com.jiahe;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CommodityService;
import com.jiahe.service.Impl.CommodityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JiaheShopApplicationTests {

    @Autowired
    CommodityService commodityService = new CommodityServiceImpl();

}
