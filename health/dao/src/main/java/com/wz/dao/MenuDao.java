package com.wz.dao;

import com.wz.pojo.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MenuDao {

    @Select("select * from t_menu where id in (select menu_id from t_role_menu where role_id = #{id})")
    List<Menu> getMenus(Integer id);

    @Select("select * from t_menu where parentMenuId = #{id}")
    List<Menu> getDauSon(Integer id);

    @Select("select * from t_menu where id in (select DISTINCT IFNULL(parentMenuId,id) from t_menu where id in (select menu_id from t_role_menu where role_id = #{id}))")
    List<Menu> getParentMenus(Integer id);

    @Select("select * from t_menu t, t_role_menu tr where t.id = tr.menu_id and tr.role_id = #{id} and level = 1")
    List<Menu> getParentMenu(Integer id);

    @Select("select t.* from t_menu t, t_role_menu tr where t.id = tr.menu_id and t.parentMenuId = #{parents} and tr.role_id = #{roleId}")
    List<Menu> getDauSons(Map<String, Integer> map);
}
