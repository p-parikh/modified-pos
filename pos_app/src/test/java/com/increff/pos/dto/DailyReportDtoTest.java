package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.model.forms.OrderItemForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportDtoTest extends AbstractUnitTest {


    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String START_TIME = "t00:00:00.000Z";
    public static final String END_TIME = "t23:59:59.000Z";

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private DailyReportDto dailyReportDto;

    @Test
    public void testGetAllDailyReports() throws ApiException, IllegalAccessException {
        createDailyReport();
        DailyReportData dailyReportData = dailyReportDto.getAll().get(0);
        assertEquals((Integer) 3, dailyReportData.getTotalOrders());
        assertEquals((Integer) 1200, dailyReportData.getTotalItems());
        assertEquals((Double) 178200.0, dailyReportData.getTotalRevenue());
    }

    @Test
    public void testGetFilteredDailyReports() throws ApiException, IllegalAccessException {
        createDailyReport();
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        DailyReportForm dailyReportForm = new DailyReportForm();
        dailyReportForm.setStartDate(currentDayDate);
        dailyReportForm.setEndDate(currentDayDate);
        DailyReportData dailyReportData = dailyReportDto.getFilteredReport(dailyReportForm).get(0);

        assertEquals((Integer) 3, dailyReportData.getTotalOrders());
        assertEquals((Integer) 1200, dailyReportData.getTotalItems());
        assertEquals((Double) 178200.0, dailyReportData.getTotalRevenue());
    }
    @Test
    public void testDailyReportScheduled(){
        dailyReportDto.dailyReportScheduled();
    }


    private void createDailyReport() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            Integer orderId = orderItemDto.create(orderItemFormList);
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        String startDate = currentDayDate + START_TIME;
        String endDate = currentDayDate + END_TIME;
        Timestamp startDateTime = Timestamp.valueOf(startDate);
        Timestamp endDateTime = Timestamp.valueOf(endDate);

        dailyReportDto.generateDailyReport(startDateTime, endDateTime);
    }

}