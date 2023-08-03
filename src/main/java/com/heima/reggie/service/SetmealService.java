package com.heima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.reggie.dto.SetmealDto;
import com.heima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    // 新增套餐，并且保存套餐和菜品关系
    public void saveWithDish(SetmealDto setmealDto);

    // 删除套餐，批量或者单个删除
    public void removeWithDish(List<Long> ids);
}
