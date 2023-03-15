package com.increff.pos.dto;

import com.increff.pos.api.DailyReportApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.dto.helper.DailyReportDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ValidationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.api.DailyReportApi.END_TIME;
import static com.increff.pos.api.DailyReportApi.START_TIME;

@Component
public class DailyReportDto {
    private static final String SCHEDULER_TIME = "* * * * * *";
    private static final Logger logger = Logger.getLogger(DailyReportDto.class);
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    @Autowired
    private DailyReportApi dailyReportApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderItemApi orderItemApi;

    public List<DailyReportData> getAll() throws ApiException {
        List<DailyReportData> dailyReportDataList = new ArrayList<>();
        List<DailyReportPojo> dailyReportPojoList = dailyReportApi.getAll();
        for(DailyReportPojo dailyReportPojo : dailyReportPojoList){
            DailyReportData dailyReportData = DailyReportDtoHelper.convertDailyReportPojoToData(dailyReportPojo);
            dailyReportDataList.add(dailyReportData);
        }
        return dailyReportDataList;
    }

    public List<DailyReportData> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException {
        ValidationUtil.checkValid(dailyReportForm);
        DailyReportDtoHelper.normalise(dailyReportForm);
        ValidationUtil.validateDates(dailyReportForm.getStartDate(), dailyReportForm.getEndDate());
        List<DailyReportData> dailyReportDataList = new ArrayList<>();
        List<DailyReportPojo> dailyReportPojoList = dailyReportApi.getFilteredReport(dailyReportForm);
        for(DailyReportPojo dailyReportPojo : dailyReportPojoList){
            dailyReportDataList.add(DailyReportDtoHelper.convertDailyReportPojoToData(dailyReportPojo));
        }
        return dailyReportDataList;
    }


    @Scheduled(cron = SCHEDULER_TIME)
    public void dailyReportScheduled() {
        //currentDateTime gets the last day date for which daily report has to be generated
        ZonedDateTime currentDateTime = ZonedDateTime.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String lastDayDate = currentDateTime.format(dateTimeFormatter);
        String startDate = lastDayDate + START_TIME;
        String endDate = lastDayDate + END_TIME;
        Timestamp startDateTime = Timestamp.valueOf(startDate);
        Timestamp endDateTime = Timestamp.valueOf(endDate);
        generateDailyReport(startDateTime, endDateTime);

    }

    public void generateDailyReport(Timestamp startDateTime, Timestamp endDateTime) {
        //orderPojoList stores all the orders which were placed and invoice was created between startDateTime and endDateTime
        List<OrderPojo> orderPojoList = orderApi.getOrderBetweenStartEndDate(startDateTime, endDateTime);
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println("\nDefault System Zone is :- \n"
                + zoneId);


        // 3. Convert java.sql.Timestamp to ZonedDateTime
        ZonedDateTime zonedStartDateTime = startDateTime.toLocalDateTime().atZone(zoneId);
        dailyReportPojo.setReportDate(zonedStartDateTime);
        Integer totalOrder = 0;
        Integer totalItem = 0;
        double totalRevenue = 0.0;
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemApi.selectByOrderId(orderPojo.getId());
            totalOrder++;
            for (OrderItemPojo orderItemPojo : orderItemPojoList) {
                totalItem += orderItemPojo.getQuantity();
                totalRevenue += (orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());
            }
        }
        dailyReportPojo.setTotalOrders(totalOrder);
        dailyReportPojo.setTotalItems(totalItem);
        dailyReportPojo.setTotalRevenue(totalRevenue);
        dailyReportApi.add(dailyReportPojo);
        logger.info("Daily Report generated successfully");
    }

}
