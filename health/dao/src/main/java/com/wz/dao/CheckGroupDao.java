package com.wz.dao;

import com.github.pagehelper.Page;
import com.wz.pojo.CheckGroup;
import com.wz.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao  {


    Page<CheckGroup> showPagesByKeyWords(String queryString);

    void add(CheckGroup checkGroup);


    @Insert("insert into t_checkgroup_checkitem values(#{checkgroup_id},#{checkitem_id})")
    void checkGroupAndCheckItem(HashMap<String, Integer> map);


    @Select("select * from t_checkgroup where id = #{id} limit 0,1")
    CheckGroup findById(int id);

    @Select("select checkitem_id  from t_checkgroup_checkitem where checkgroup_id = #{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void deleteCheckItemIdsById(int id);


    void edit(CheckGroup checkGroup);


    @Delete("delete from t_checkgroup where id = #{id}")
    void deleteById(int id);

    @Delete(" delete from t_setmeal_checkgroup where checkgroup_id = #{id}")
    void deletesetmeal_checkgroupByGroupId(int id);

    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();


    List<CheckGroup> findCheckGroupBySetMealId(int id);
}
