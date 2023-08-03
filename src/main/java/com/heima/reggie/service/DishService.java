package com.heima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.reggie.dto.DishDto;
import com.heima.reggie.entity.Dish;


public interface DishService extends IService<Dish> {
    // 新增菜品，同时新增菜品对应的口味数据，需要操作两张表dish、dishflover
    public void saveWithFlavor(DishDto dishDto);

    // 根据id查询对应的菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(long id);

    // 更新菜品数据
    public void updateWithDisdto(DishDto dishDto);
}
