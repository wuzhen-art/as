package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.entity.Result;
import com.wz.pojo.Setmeal;
import com.wz.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {


    @Reference(interfaceClass = SetMealService.class)
    private SetMealService setMealService;


    @RequestMapping("/getSetMeal")
    public Result getSetMeal(){
        try {
            List<Setmeal> list = setMealService.listAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

        }


    }

    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setMealService.getSetMealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);

        }

    }
}
