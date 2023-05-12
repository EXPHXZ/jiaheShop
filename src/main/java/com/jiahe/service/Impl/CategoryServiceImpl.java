package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.dao.CategoryDao;
import com.jiahe.pojo.Category;
import com.jiahe.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

}
