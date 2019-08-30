package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wz.constants.MessageConstant;
import com.wz.utils.POIUtils;
import com.wz.entity.Result;
import com.wz.pojo.OrderSetting;
import com.wz.service.OrderSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/orderSetting")

public class OrderSettingController {

    @Reference(interfaceClass = OrderSettingService.class)
    private OrderSettingService orderSettingService;


    @RequestMapping("/setAmountByOrderDate")
    public Result setAmountByOrderDate(@RequestBody OrderSetting orderSetting){

        try {
            orderSettingService.setAmountByOrderDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }

    @RequestMapping("/getCurrentMonthOrderSetting")
    public Result getCurrentMonthOrderSetting(String date){
        try {
            //System.out.println(date);
            List<OrderSetting> list =  orderSettingService.getCurrentMonthOrderSetting(date);
            List<Map> list1 = new ArrayList<Map>();
            for (OrderSetting orderSetting : list) {
                HashMap map = new HashMap<>();
                map.put("orderDate",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());
                list1.add(map);
            }
           // System.out.println(list1);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile ){

//        POI解析文件
        try {
            List<String[]> excel = POIUtils.readExcel(excelFile);

//            建立list
            if (excel != null && excel.size()>0){
                List<OrderSetting> list = new ArrayList<>();
//                System.out.println(list);
                for (String[] strings : excel) {
                    System.out.println(Arrays.toString(strings));
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]),
                            Integer.parseInt(strings[2]));


                    list.add(orderSetting);
                }
                orderSettingService.add(list);


            }

            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);

        }


    }





}
