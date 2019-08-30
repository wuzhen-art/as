package com.wz.service;

import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.CheckGroup;
import com.wz.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(CheckGroup checkGroup, int[] checkitemIds);

    CheckGroup findById(int id);

    List<Integer> getCheckItemIdsByCheckGroupId(int id);

    void edit(int[] checkItemIds, CheckGroup checkGroup);

    void delete(int id);

    List<CheckGroup> findAll();
}
