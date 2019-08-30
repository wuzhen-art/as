package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.MenuDao;
import com.wz.dao.PermissionDao;
import com.wz.dao.RoleDao;
import com.wz.dao.UserDao;
import com.wz.pojo.Menu;
import com.wz.pojo.Permission;
import com.wz.pojo.Role;
import com.wz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImp implements UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private MenuDao menuDao;


    @Override
    public User findUserByUsername(String username) {
        User user = getRoleSets(username);
        Set<Role> roleSets = user.getRoles();
        if (roleSets!=null && roleSets.size()>0){
                for (Role role : roleSets) {
                    Set<Permission> permissions = permissionDao.findPermissionByRid(role.getId());
                    role.setPermissions(permissions);
                }
        }
        return user;
    }

    @Override
    public List<Menu> getMenu(String username) {
        User roleSets = getRoleSets(username);
        Set<Role> roles = roleSets.getRoles();
        List<Menu> menus = null;

          for (Role role : roles){
              Integer roleId = role.getId();
              menus = menuDao.getParentMenus(roleId);
              for (Menu menu : menus) {
                  Integer parents = menu.getId();
                  Map<String, Integer> map = new HashMap<String, Integer>();
                  map.put("roleId",roleId);

                  map.put("parents",parents);
                  List<Menu> menuList = menuDao.getDauSons(map);
                  menu.setChildren(menuList);



              }
////                获取role 对应得 menu 表
////                查询每个menu的parentId时多个子menu.
//                menus = menuDao.getMenus(role.getId());
//                for (Menu menu : menus) {
//                    List<Menu> menuList = menuDao.getDauSon(menu.getId());
//                    if (menuList!=null){
//                        menu.setChildren(menuList);
//                    }
//                }
//            }
       }
        return menus;


    }

    @Override
    public void modifyPwd(Map data,String name) throws Exception {
        User user = userDao.findUserByUsername(name);


        String oldPassword = (String) data.get("oldPassword");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        oldPassword = encoder.encode(oldPassword);
        String newPassword = (String) data.get("newPassword");
//        判断就密码的正确性
        if (!user.getPassword().equals(oldPassword)){
            throw new Exception("密码不正确");

        }else {
            newPassword = encoder.encode(newPassword);
            user.setPassword(newPassword);
            userDao.updateUser(user);


        }


    }

    public User getRoleSets(String username){
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            Set<Role> roleSet = roleDao.findRolesByUid(user.getId());
            user.setRoles(roleSet);
        }
        return user;


    }

}
