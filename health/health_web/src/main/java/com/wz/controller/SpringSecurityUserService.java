package com.wz.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.pojo.Permission;
import com.wz.pojo.Role;
import com.wz.pojo.User;
import com.wz.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component("springSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {


    @Reference(interfaceClass = UserService.class)
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
        * 获取用户的用户名和密码和权限,封装入userDetails
        * */
        User user = userService.findUserByUsername(username);
        if (user!=null){
            ArrayList<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<GrantedAuthority>();
            Set<Role> roles = user.getRoles();
            if (roles!=null && roles.size()>0){
                for (Role role : roles) {
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions!=null && permissions.size()>0){
                        for (Permission permission : permissions) {
                            grantedAuthorityArrayList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                        }


                    }

                }
            }
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), grantedAuthorityArrayList);
            return userDetails;

        }
        return null;
    }
}
