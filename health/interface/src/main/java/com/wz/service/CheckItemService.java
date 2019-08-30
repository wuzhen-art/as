package com.wz.service;

import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(CheckItem checkItem);

    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    void delete(int id);

    List<CheckItem> findAll();
}
