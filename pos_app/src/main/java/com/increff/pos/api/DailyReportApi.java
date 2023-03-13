package com.increff.pos.api;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.DailyReportForm;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DailyReportApi {

    public static final String START_TIME = "t00:00:00.000Z";
    public static final String END_TIME = "t23:59:59.000Z";

    @Autowired
    private DailyReportDao dailyReportDao;

    public void add(DailyReportPojo dailyReportPojo) {
        dailyReportDao.insert(dailyReportPojo);
    }

    public List<DailyReportPojo> getAll() throws ApiException {
        List<DailyReportPojo> dailyReportPojoList = dailyReportDao.viewAll();
        dailyReportPojoList.sort((dailyReportPojo1, dailyReportPojo2) -> dailyReportPojo2.getReportDate().compareTo(dailyReportPojo1.getReportDate()));
        return dailyReportPojoList;
    }

    public List<DailyReportPojo> getFilteredReport(DailyReportForm dailyReportForm) throws ApiException {
        String startDateTime = dailyReportForm.getStartDate() + START_TIME;
        String endDateTime = dailyReportForm.getEndDate() + END_TIME;
        ZonedDateTime zonedStartDateTime;
        ZonedDateTime zonedEndDateTime;
        try{
            zonedStartDateTime = ZonedDateTime.parse(startDateTime);
            zonedEndDateTime = ZonedDateTime.parse(endDateTime);
        } catch (Exception e) {
            throw new ApiException("Invalid date format");
        }

        return dailyReportDao.selectFilteredReport(zonedStartDateTime, zonedEndDateTime);
    }
}
