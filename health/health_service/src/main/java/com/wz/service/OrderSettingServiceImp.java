package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.OrderSettingDao;
import com.wz.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImp implements OrderSettingService {


    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void setAmountByOrderDate(OrderSetting orderSetting) {
        /*如果orderSetting表中有日期,则修改
        * 反之则添加数据
        * */
        int number = orderSettingDao.findNumber(orderSetting.getOrderDate());
        if (number > 0){
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.addOrderDate(orderSetting);
        }


    }

    @Override
    public List<OrderSetting> getCurrentMonthOrderSetting(String date) {
        /*
        * 初始日和最后日
        * 放入map中
        *
        * */
        String start_date = date + "-01";
        String end_date = date + "-31";
        HashMap<String, String> map = new HashMap<>();
        map.put("start_date",start_date);
        map.put("end_date",end_date);
        List<OrderSetting> list = orderSettingDao.getCurrentMonthOrderSetting(map);
        return list;

    }

    @Override
    public void add(List<OrderSetting> list) {

//        判断list是否为空
        if (list != null && list.size()>0){
            for (OrderSetting orderSetting : list) {
//                查询orderDate是否存在,调用之前方法
                int number = orderSettingDao.findNumber(orderSetting.getOrderDate());
                if (number > 0){
                    orderSettingDao.updateNumberAndReservationsByOrderDate(orderSetting);
                }else {
                    orderSettingDao.addOrderDate(orderSetting);
                }

            }
        }


    }
}
