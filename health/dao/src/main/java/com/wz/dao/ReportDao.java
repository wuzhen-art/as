package com.wz.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ReportDao {

//    @Select("select count(*) from t_member where regTime &lt;= #{month}")
    Integer queryAccount(String month);


    List<Map<String,Object>> getMealReport();

    List<Map<String, Object>> getHotSetmeal();
}
