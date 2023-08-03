package com.heima.reggie.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.reggie.common.BaseContext;
import com.heima.reggie.common.R;
import com.heima.reggie.entity.AddressBook;
import com.heima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址簿管理
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookControllor {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址成功
     * @param address
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook address){
        address.setUserId(BaseContext.getCurrentId());

        addressBookService.save(address);

        return R.success("添加成功");
    }

    /**
     * 获取该用户的所有地址
     * @return
     */
    @GetMapping("/list")
    public R<List> list(){
        LambdaQueryWrapper<AddressBook> eq = Wrappers.lambdaQuery(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(eq);

        if(list == null){
            return R.error("没有找到任何地址");
        }

        return R.success(list);
    }

    /**
     * 设置默认地址
     * @param address
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> defaultId(@RequestBody AddressBook address){
        //默认地址只有一个
        //把该用户的所有地址设置为is_default = 0（相当于清空默认）
        LambdaUpdateWrapper<AddressBook> set = Wrappers.lambdaUpdate(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .set(AddressBook::getIsDefault, 0);
        //UPDATE address_book SET is_default = 0 WHERE user_id = ?;
        addressBookService.update(set);

        //再将默认地址设为is_default = 0
        address.setIsDefault(1);

        //UPDATE address_book SET is_default = 1 WHERE id = ?;
        addressBookService.updateById(address);

        return R.success(address);
    }

    /**
     * 查询单个地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook byId = addressBookService.getById(id);

        if(byId == null){
            return R.error("没有找到该对象");
        }

        return R.success(byId);
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getOne(){
        LambdaQueryWrapper<AddressBook> eq = Wrappers.lambdaQuery(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, 1);

        AddressBook one = addressBookService.getOne(eq);

        if(one == null){
            return R.error("没有找到该对象");
        }

        return R.success(one);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);

        return R.success("修改成功");
    }

    /**
     * 获取最新地址
     * @return
     */
    @GetMapping("/lastUpdate")
    public R<AddressBook> lastUpdate(){
        LambdaQueryWrapper<AddressBook> one = Wrappers.lambdaQuery(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .orderByDesc(AddressBook::getUpdateTime)
                .last("limit 1");

        AddressBook one1 = addressBookService.getOne(one);

        return R.success(one1);
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        LambdaQueryWrapper<AddressBook> eq = Wrappers.lambdaQuery(AddressBook.class)
                .eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getId,ids);

        addressBookService.remove(eq);

        return R.success("删除成功");
    }
}