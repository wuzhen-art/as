package com.wz.dao;

import com.wz.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface MemberDao {


    @Select("select * from t_member where phoneNumber = #{telephone}")
    Member getMemberByTelephone(String telephone);


    void add(Member member1);

    @Select("select count(phoneNumber) from t_member where regTime = #{today}")
    Integer getByDay(String today);

    @Select("select count(phoneNumber) from t_member")
    Integer getAll();

    Integer getAfterDayAmount(String thisWeekMonday);
}
