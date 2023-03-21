package com.increff.pos.dto;

import com.increff.pos.api.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.increff.pos.api.DailyReportApi.END_TIME;
import static com.increff.pos.api.DailyReportApi.START_TIME;

@Component
public class ReportDto {

    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderItemApi orderItemApi;

    public void getInventoryReportData(HttpServletResponse httpServletResponse) throws Exception {
        List<String> resultSet = new ArrayList<>();

        List<InventoryPojo> data = inventoryApi.getAllEntries();
        String firstRow = "Barcode,Brand,Category,Quantity";
        resultSet.add(firstRow);
        for(InventoryPojo temp : data){
            String row = "";
            ProductPojo productPojo = productApi.getById(temp.getProductId());
            row += productPojo.getBarcode() + ",";
            BrandPojo brandPojo = brandApi.getById(productPojo.getBrandCategory());
            row += brandPojo.getBrand() + ",";
            row += brandPojo.getCategory() +",";
            row += temp.getQty();
            resultSet.add(row);
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : resultSet){
            printWriter.println(temp);
        }
        printWriter.flush();
        httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");
        httpServletResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().write(stringWriter.toString());
    }

    public void getBrandReportData(HttpServletResponse httpServletResponse) throws Exception{
        List<String> resultSet = new ArrayList<>();

        List<BrandPojo> data = brandApi.getAllEntries();
        String firstRow = "Brand, Category";
        resultSet.add(firstRow);

        for(BrandPojo temp : data){
            String row = "";
            row += temp.getBrand() + ",";
            row += temp.getCategory();
            resultSet.add(row);
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : resultSet){
            printWriter.println(temp);
        }
        printWriter.flush();
        httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");
        httpServletResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().write(stringWriter.toString());
    }

    public void getSalesReportData(String startDateString, String endDateString, HttpServletResponse httpServletResponse) throws ApiException, IOException {
        String startDate = startDateString + START_TIME;
        String endDate = endDateString + END_TIME;
        ZonedDateTime zonedStartDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime zonedEndDateTime = ZonedDateTime.parse(endDate);
        List<String> resultSet = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderApi.getOrderBetweenStartEndDate(zonedStartDateTime, zonedEndDateTime);
        HashMap<Integer, Long> hmQty = new HashMap<>();
        HashMap<Integer, Double> hmRevenue = new HashMap<>();
        int count = 0;
        for(OrderPojo i : orderPojoList){
            count++;
            List<OrderItemPojo> orderItemPojoList = orderItemApi.selectByOrderId(i.getId());
            for(OrderItemPojo j : orderItemPojoList){
              ProductPojo productPojo = productApi.getById(j.getProductId());
                hmQty.put(productPojo.getBrandCategory(),
                        hmQty.getOrDefault(productPojo.getBrandCategory(), (long) 0) + j.getQuantity());
                hmRevenue.put(productPojo.getBrandCategory(),
                        hmRevenue.getOrDefault(productPojo.getBrandCategory(), (double) 0) + j.getQuantity() * j.getSellingPrice());
            }
        }
        System.out.println("Count: " +count);

        String firstRow = "Brand,Category,Quantity,Revenue";
        resultSet.add(firstRow);
        for(Map.Entry<Integer, Long> mapElement : hmQty.entrySet()){
            BrandPojo brandPojo = brandApi.getById(mapElement.getKey());
            String row = "";
            row += brandPojo.getBrand() + ",";
            row += brandPojo.getCategory() + ",";
            row += mapElement.getValue() + ",";
            row += hmRevenue.get(mapElement.getKey());
            resultSet.add(row);
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : resultSet){
            printWriter.println(temp);
        }
        printWriter.flush();
        httpServletResponse.addHeader(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8");
        httpServletResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.getWriter().write(stringWriter.toString());
    }
}
