package com.wz.controller;


import com.aliyuncs.exceptions.ClientException;
import com.wz.constants.MessageConstant;
import com.wz.constants.RedisConstant;
import com.wz.constants.RedisMessageConstant;
import com.wz.entity.Result;
import com.wz.utils.SMSUtils;
import com.wz.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer checkCode = ValidateCodeUtils.generateValidateCode(4);

        try {
            SMSUtils.sendShortMessage2(telephone,checkCode.toString());
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone,
                    60*5,checkCode.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,checkCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);

        }

    }


    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage2(telephone,validateCode.toString());
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone,
                    60*5,validateCode.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
