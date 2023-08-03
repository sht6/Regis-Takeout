package com.heima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(long id);
}
