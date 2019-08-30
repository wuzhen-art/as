package com.wz.service;

import com.wz.entity.Result;
import com.wz.pojo.Order;

import java.util.Map;

public interface OrderService {
    Result process(Map orderInfo) throws Exception;

    Map findById(int id);
}
