package com.wz.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import com.wz.constants.MessageConstant;
import com.wz.entity.Result;
import com.wz.service.ReportService;
import com.wz.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/report")
public class ReportController {


    @Reference(interfaceClass = ReportService.class)
    private ReportService reportService;


    @RequestMapping("/getMemberReport")
    public Result getMemberReport(@RequestParam(required = false) String dateRange){



//        获得过去12月的月份
//        逐个添加
//        获得每月的用户量
//         返结果


        try {
            List<String> monthList = new ArrayList<>();
            if (dateRange.length()>0 ){
                String[] split = dateRange.split(",");
                String begin = split[0];
                String end = split[1];
                System.out.println(begin);
                System.out.println(end);
                monthList = DateUtils.getMonthBetween(begin, end, "yyyy-MM");
                System.out.println(dateRange);
            }else {
                //            获得日期
                Calendar calendar = Calendar.getInstance();
//            获得12月前
                calendar.add(Calendar.MONTH,-12);
                for (int i = 0; i < 12 ; i++) {
//                添加
                    calendar.add(Calendar.MONTH,1);
//                简单常用的格式化月份
                    monthList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));

                }
            }
            Map<String, List> map = new HashMap<>();
            map.put("months",monthList);

            List<Integer> amountList =  reportService.getAmount(monthList);

            map.put("memberCount",amountList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);

        }

    }

    //传递一个map数据包含setmeal and  相应的order数量

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            Map map = reportService.getSetmealReport();
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);

        }

    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map map = reportService.getBusinessReport();
            return new Result(true,MessageConstant.EDIT_MEMBER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MEMBER_FAIL);

        }

    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        /*
        * 1 获得本报告数据
        * 2 获得xml文件模板的字节流
        * 3 创建工作薄,获得表对象
        * 4 获得行列,填充表
        * 5 通过文件流响应,setHeader
        * */
        try {
            Map businessReport = reportService.getBusinessReport();
            String reportDate = (String) businessReport.get("reportDate");
//            reportDate:null,    //日期
            Integer todayNewMember = (Integer) businessReport.get("todayNewMember");
//                    todayNewMember :0,  //今天新增会员数(统计今天的会员总数量)
            Integer totalMember = (Integer) businessReport.get("totalMember");
//                    totalMember :0,     //总会员数
            Integer thisWeekNewMember = (Integer) businessReport.get("thisWeekNewMember");
//                    thisWeekNewMember :0, //本周新增会员数
            Integer thisMonthNewMember = (Integer) businessReport.get("thisMonthNewMember");
//                    thisMonthNewMember :0, //本月新增会员数
            Integer todayOrderNumber = (Integer) businessReport.get("todayOrderNumber");
//                    todayOrderNumber :0,  //今日预约数
            Integer todayVisitsNumber = (Integer) businessReport.get("todayVisitsNumber");
//                    todayVisitsNumber :0, //今日到诊数
            Integer thisWeekOrderNumber = (Integer) businessReport.get("thisWeekOrderNumber");
//                    thisWeekOrderNumber :0, //本周预约数
            Integer thisWeekVisitsNumber = (Integer) businessReport.get("thisWeekVisitsNumber");
//                    thisWeekVisitsNumber :0,//本周到诊数
            Integer thisMonthOrderNumber = (Integer) businessReport.get("thisMonthOrderNumber");
//                    thisMonthOrderNumber :0, //本月预约数
            Integer thisMonthVisitsNumber = (Integer) businessReport.get("thisMonthVisitsNumber");
//                    thisMonthVisitsNumber :0, //本月到诊数
//                    hotSetmeal :[ //热门套餐(四个)
            List<Map<String,Object>> hotSetmeal = (List<Map<String, Object>>) businessReport.get("hotSetmeal");
//            {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
//            {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}]
//            获得模板流
            InputStream resourceAsStream = request.getSession().getServletContext().getResourceAsStream("template/report_template.xlsx");
//            创建工作表
            XSSFWorkbook sheets = new XSSFWorkbook(resourceAsStream);
//            获得表对象
            XSSFSheet sheet = sheets.getSheetAt(0);
//            填充今日日期
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
//            填充会员统计数据
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);

//            到诊统计
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);
//            套餐统计
            int rowNumber = 12;
            for (Map<String, Object> meal : hotSetmeal) {
                String name = (String) meal.get("name");
                Long setmeal_count = (Long) meal.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) meal.get("proportion");
                row = sheet.getRow(rowNumber++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }

//            设置header
            response.setContentType("application/ms-excel");
            response.setHeader("Content-Disposition","attachment;filename=report.xlsx");

//            获得文件流
            ServletOutputStream outputStream = response.getOutputStream();
            sheets.write(outputStream);
            outputStream.close();
            resourceAsStream.close();
            sheets.close();

            return null;




            /*
             * 2 获得xml文件模板的字节流
             * 3 创建工作薄,获得表对象
             * 4 获得行列,填充表
             * 5 通过文件流响应,setHeader
             * */
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }



}
