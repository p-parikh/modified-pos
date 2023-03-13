package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class DailyReportApiTest extends AbstractUnitTest {

    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    @Autowired
    private DailyReportApi dailyReportApi;
    @Test
    public void testAdd() throws ApiException {
        DailyReportPojo dailyReportPojo = createDailyReportPojo();
        dailyReportApi.add(dailyReportPojo);

        matchPojo(dailyReportApi.getAll().get(0));
    }

    @Test
    public void testGetFilteredReport() throws ApiException {
        DailyReportPojo dailyReportPojo = createDailyReportPojo();
        dailyReportApi.add(dailyReportPojo);
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        DailyReportForm dailyReportForm = new DailyReportForm();
        dailyReportForm.setStartDate(currentDayDate);
        dailyReportForm.setEndDate(currentDayDate);
        matchPojo(dailyReportApi.getFilteredReport(dailyReportForm).get(0));
    }



    private void matchPojo(DailyReportPojo dailyReportPojo){
        assertEquals((Integer) 3,dailyReportPojo.getTotalOrders());
        assertEquals((Integer) 1200,dailyReportPojo.getTotalItems());
        assertEquals((Double) 178200.0,dailyReportPojo.getTotalRevenue());
    }

    private DailyReportPojo createDailyReportPojo() {
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setReportDate(ZonedDateTime.now());
        dailyReportPojo.setTotalOrders(3);
        dailyReportPojo.setTotalItems(1200);
        dailyReportPojo.setTotalRevenue(178200.0);
        return dailyReportPojo;
    }

}
