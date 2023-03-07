package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api
@RestController
@RequestMapping(path = "/report")
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value= "Get Inventory report")
    @RequestMapping(path= "/inventoryReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getInventoryReport() throws Exception{

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : reportDto.getInventoryReportData()){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    @ApiOperation(value= "Get Brand report")
    @RequestMapping(path= "/brandReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getBrandReport() throws Exception{

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : reportDto.getBrandReportData()){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    @ApiOperation(value= "Get Sales report")
    @RequestMapping(path="/salesReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getSalesReport(@RequestParam String startDateString, @RequestParam String endDateString) throws Exception{
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = dateFormat.parse(startDateString);
        Timestamp startDate = new java.sql.Timestamp(parsedStartDate.getTime());
        Date parsedEndDate = dateFormat.parse(endDateString);
        Timestamp endDate = new java.sql.Timestamp(parsedEndDate.getTime());

        for(String temp : reportDto.getSalesReportData(startDate,endDate)){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }
}
