package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.constants.RedisMessageConstant;
import com.wz.entity.Result;
import com.wz.pojo.Order;
import com.wz.service.OrderService;
import com.wz.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.xml.ws.RequestWrapper;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference(interfaceClass = OrderService.class)
    private OrderService orderService;



    @RequestMapping("/submit")
    public Result submit(@RequestBody Map orderInfo){

//        认证验证码

        try {
            String telephone = (String)orderInfo.get("telephone");
            String validateCode = (String)orderInfo.get("validateCode");
            String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone);
            if ( code == null || !code.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }

//        进行预约
            Result result = orderService.process(orderInfo);

            if (result.isFlag()){
//            通知
                String orderDate = (String) orderInfo.get("orderDate");
                System.out.println(orderDate);
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Map order = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
