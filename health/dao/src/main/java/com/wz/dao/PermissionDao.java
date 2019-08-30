package com.wz.dao;

import com.wz.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {


    @Select("select * from t_permission where id in (select permission_id from t_role_permission where role_id = #{id} )")
    Set<Permission> findPermissionByRid(Integer id);
}
