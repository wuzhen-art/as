package com.wz.dao;

import com.github.pagehelper.Page;
import com.wz.pojo.Setmeal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import javax.ws.rs.DELETE;
import java.util.HashMap;
import java.util.List;

public interface SetMealDao {
    Page<Setmeal> findPagesByCondition(String queryString);

    void add(Setmeal setmeal);



    @Insert("insert into t_setmeal_checkgroup values(#{setmeal_id},#{checkgroup_id})")
    void checkGroupAndSetmeal(HashMap<String, Integer> map);

    @Select("select * from t_setmeal")
    List<Setmeal> findAll();


    Setmeal getSetMealById(int id);

    @Select("select * from t_setmeal where id = #{id}")
    Setmeal findById(int id);


    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{id}")
    List<Integer> getCheckGroupsBySetMealId(int id);

    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{id}")
    void delete(int id);

    void edit(Setmeal setmeal);

    @Delete("delete from t_setmeal where id = #{id}")
    void deleteSetMeal(int id);
}
