package com.heima.reggie.dto;


import com.heima.reggie.entity.Setmeal;
import com.heima.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
