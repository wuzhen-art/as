package com.wz.service;

import com.wz.pojo.OrderSetting;

import java.util.List;

public interface OrderSettingService {
    void setAmountByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> getCurrentMonthOrderSetting(String date);

    void add(List<OrderSetting> list);
}
