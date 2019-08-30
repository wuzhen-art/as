package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.entity.Result;
import com.wz.service.LoginService;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {


    @Reference(interfaceClass = LoginService.class)
    private LoginService loginService;

    @RequestMapping("/check")
    public Result check(@RequestBody Map loginInfo, HttpServletRequest request){

//        获得loginInfo
        try {
            Result result = loginService.check(loginInfo);
            request.getSession().setAttribute("member",result.getData());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "服务器异常");
        }


    }
}
