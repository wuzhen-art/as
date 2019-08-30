package com.wz.dao;

import com.wz.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    @Select("select * from t_user where username = #{username}")
    User findUserByUsername(String username);

    void updateUser(User user);
}
