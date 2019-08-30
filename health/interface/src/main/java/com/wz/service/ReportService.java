package com.wz.service;


import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Integer> getAmount(List<String> monthList);

    Map getSetmealReport();

    Map getBusinessReport() throws Exception;
}
