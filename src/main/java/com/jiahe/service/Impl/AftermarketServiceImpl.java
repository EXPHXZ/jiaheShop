package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.dao.AftermarketDao;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Aftermarket;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.service.AftermarketService;
import org.springframework.stereotype.Service;

/**
 * @author  
 * @since 2023-04-29
 */

@Service
public class AftermarketServiceImpl extends ServiceImpl<AftermarketDao, Aftermarket> implements AftermarketService {

    private AftermarketDao aftermarketDao;
    @Override
    public Boolean addAftermarket (Aftermarket aftermarket){
        LambdaQueryWrapper<Aftermarket> lqw = new LambdaQueryWrapper<>();
        return aftermarketDao.insert(aftermarket) > 0;
    }
}

