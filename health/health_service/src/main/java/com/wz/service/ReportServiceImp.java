package com.wz.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.wz.dao.MemberDao;
import com.wz.dao.OrderDao;
import com.wz.dao.ReportDao;
import com.wz.pojo.Member;
import com.wz.pojo.Order;
import com.wz.utils.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImp implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Integer> getAmount(List<String> monthList) {
        List<Integer> list = new ArrayList<>();
        for (String month : monthList) {
            month = month + ".31";
            Integer amount = reportDao.queryAccount(month);
            list.add(amount);
        }
        return list;

    }

    @Override

    public Map getSetmealReport() {
        List<Map<String,Object>> list = reportDao.getMealReport();
        ArrayList<String> setmealNames = new ArrayList<>();
       // ArrayList<Long> setmealCount = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String name = (String)map.get("name");
            setmealNames.add(name);
            //Long amount = (Long)map.get("amount");
            //setmealCount.add(amount);

        }
        Map<String, List> stringListMap = new HashMap<>();
        stringListMap.put("setmealNames",setmealNames);
        stringListMap.put("setmealCount",list);




        return stringListMap;
    }

    @Override
    public Map getBusinessReport() throws Exception {
        String today = DateUtils.parseDate2String( DateUtils.getToday());
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        String firstDayThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        HashMap<String, Object> map = new HashMap<>();
//        reportDate:null,    //日期
//                todayNewMember :0,  //今天新增会员数(统计今天的会员总数量)
        map.put("reportDate",today);
        System.out.println(map.get("reportDate"));
        Integer todayNewMember = memberDao.getByDay(today);
        map.put("todayNewMember",todayNewMember);
//                totalMember :0,     //总会员数
        Integer totalMember = memberDao.getAll();
        map.put("totalMember",totalMember);
//                thisWeekNewMember :0, //本周新增会员数
        Integer thisWeekNewMember = memberDao.getAfterDayAmount(thisWeekMonday);
        map.put("thisWeekNewMember",thisWeekNewMember);
//                thisMonthNewMember :0, //本月新增会员数
        Integer thisMonthNewMember = memberDao.getAfterDayAmount(firstDayThisMonth);
        map.put("thisMonthNewMember",thisMonthNewMember);
//                todayOrderNumber :0,  //今日预约数
        Integer todayOrderNumber = orderDao.findByDay(today);
        map.put("todayOrderNumber",todayOrderNumber);
//                todayVisitsNumber :0, //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsByDay(today);
        map.put("todayVisitsNumber",todayVisitsNumber);
//                thisWeekOrderNumber :0, //本周预约数
        Integer thisWeekOrderNumber = orderDao.getAfterDayAmount(thisWeekMonday);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
//                thisWeekVisitsNumber :0,//本周到诊数
        Integer thisWeekVisitsNumber = orderDao.getReservationsAfterDayAmount(thisWeekMonday);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
//                thisMonthOrderNumber :0, //本月预约数
        Integer thisMonthOrderNumber = orderDao.getAfterDayAmount(firstDayThisMonth);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
//                thisMonthVisitsNumber :0, //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.getReservationsAfterDayAmount(firstDayThisMonth);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
//                hotSetmeal :[ //热门套餐(四个)
        List<Map<String, Object>> hotSetmeal = reportDao.getHotSetmeal();
        map.put("hotSetmeal",hotSetmeal);
//        {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
//        {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
        return map;

    }





}
