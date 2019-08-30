package com.wz.dao;

import com.wz.pojo.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findByOrder(Order order);


    void add(Order order);


    Map findById(int id);

    @Select("select count(id) from t_order where orderDate = #{today}")
    Integer findByDay(String today);

    @Select("select count(id) from t_order where orderDate = #{today} and orderStatus = '已到诊'")
    Integer findVisitsByDay(String today);

    Integer getAfterDayAmount(String thisWeekMonday);

    Integer getReservationsAfterDayAmount(String thisWeekMonday);
}
