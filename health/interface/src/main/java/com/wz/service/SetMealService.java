package com.wz.service;

import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.Setmeal;

import java.util.List;

public interface SetMealService  {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(int[] checkgroupIds, Setmeal setmeal);

    List<Setmeal> listAll();

    Setmeal getSetMealById(int id);

    Setmeal findById(int id);

    List<Integer> findCheckGroupsBySetMealId(int id);

    void edit(int[] checkgroupIds, Setmeal setmeal);

    void delete(int id);
}
