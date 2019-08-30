package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.entity.Result;
import com.wz.pojo.Menu;
import com.wz.pojo.Role;
import com.wz.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {


    @Reference(interfaceClass = UserService.class)
    private UserService userService;




    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_USERNAME_FAIL);

        }


    }

    @RequestMapping("/getMenu")
    public Result getMenu(){
        Result result = getUsername();
        String username = (String)result.getData();
        try {
            List<Menu> menu = userService.getMenu(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MENU_FAIL);

        }


    }

    @RequestMapping("/modifyPwd")
    public Result modifyPwd(@RequestParam Map data){
        Result user = getUsername();

        try {
            String name = (String) user.getData();
            userService.modifyPwd(data,name);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }


    }


}
