package com.wz.dao;

import com.github.pagehelper.Page;
import com.wz.entity.PageResult;
import com.wz.entity.QueryPageBean;
import com.wz.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CheckItemDao {


    Page<CheckItem> queryByCondition(String queryString);

    @Insert("insert into t_checkitem values(null,#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    int add(CheckItem checkItem);

    @Select("select * from t_checkitem where id = #{id}")
    CheckItem findById(int id);


    void edit(CheckItem checkItem);


    @Delete("delete from t_checkitem where id = #{id}")
    void delete(int id);

    @Select("select * from t_checkitem ")
    List<CheckItem> findAll();


    @Delete("delete from t_checkgroup_checkitem where checkitem_id = #{id}")
    void deleteCheckGroupCheckItemBycheckItemId(int id);

    List<CheckItem> findByGroupId(int id);
}
