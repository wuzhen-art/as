package com.wz.service;

import com.wz.pojo.Menu;
import com.wz.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findUserByUsername(String username);

    List<Menu> getMenu(String username);

    void modifyPwd(Map data,String name) throws Exception;
}
