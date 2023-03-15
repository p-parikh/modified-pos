package com.increff.pos.dto;

import com.increff.pos.api.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ReportDto {

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

    public ResponseEntity<Object> getInventoryReportData() throws Exception {
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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    public ResponseEntity<Object> getBrandReportData() throws Exception{
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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    public ResponseEntity<Object> getSalesReportData(String startDateString, String endDateString) throws ApiException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = dateFormat.parse(startDateString);
        Timestamp startDate = new java.sql.Timestamp(parsedStartDate.getTime());
        Date parsedEndDate = dateFormat.parse(endDateString);
        Timestamp endDate = new java.sql.Timestamp(parsedEndDate.getTime());

        List<String> resultSet = new ArrayList<>();
        List<OrderPojo> orderPojoList = orderApi.getOrderBetweenStartEndDate(startDate, endDate);
        HashMap<Integer, Long> hmQty = new HashMap<>();
        HashMap<Integer, Double> hmRevenue = new HashMap<>();
        for(OrderPojo i : orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemApi.selectByOrderId(i.getId());
            for(OrderItemPojo j : orderItemPojoList){
              ProductPojo productPojo = productApi.getById(j.getProductId());
                hmQty.put(productPojo.getBrandCategory(),
                        hmQty.getOrDefault(productPojo.getBrandCategory(), (long) 0) + j.getQuantity());
                hmRevenue.put(productPojo.getBrandCategory(),
                        hmRevenue.getOrDefault(productPojo.getBrandCategory(), (double) 0) + j.getQuantity() * j.getSellingPrice());
            }
        }

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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }
}
