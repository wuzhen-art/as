package com.wz.dao;

import com.wz.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OrderSettingDao {


    @Select("select count(*) from t_ordersetting where orderDate = #{orderDate}")
    int findNumber(Date orderDate);

    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void updateNumberByOrderDate(OrderSetting orderSetting);

    @Insert("insert into t_ordersetting(number,orderDate,reservations) values(#{number},#{orderDate},#{reservations})")
    void addOrderDate(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderDate between #{start_date} and #{end_date}")
    List<OrderSetting> getCurrentMonthOrderSetting(HashMap<String, String> map);

    @Update("update t_ordersetting set number = #{number},reservations = #{reservations} where orderDate = #{orderDate}")
    void updateNumberAndReservationsByOrderDate(OrderSetting orderSetting);


    @Select("select * from t_ordersetting where orderDate = #{date}")
    OrderSetting findByDate(Date date);

    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    void editReservations(OrderSetting orderSetting);
}
