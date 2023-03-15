package com.increff.pos.dto.helper;

import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

public class DailyReportDtoHelper {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    public static DailyReportData convertDailyReportPojoToData(DailyReportPojo dailyReportPojo){
        DailyReportData dailyReportData = new DailyReportData();
        dailyReportData.setTotalItems(dailyReportPojo.getTotalItems());
        dailyReportData.setTotalRevenue(dailyReportPojo.getTotalRevenue());
        dailyReportData.setTotalOrders(dailyReportPojo.getTotalOrders());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String reportDate = dailyReportPojo.getReportDate().format(dateTimeFormatter);
        dailyReportData.setDate(reportDate);
        return dailyReportData;
    }

    public static DailyReportForm normalise(DailyReportForm dailyReportForm){
        dailyReportForm.setEndDate(dailyReportForm.getEndDate().toLowerCase().trim());
        dailyReportForm.setStartDate(dailyReportForm.getStartDate().toLowerCase().trim());
        return dailyReportForm;
    }
}
