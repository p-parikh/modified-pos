package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{

    private static final String SELECT_ALL = "select dr from Dailyreport dr";
    private static final String SELECT_FILTERED = "select dr from Dailyreport dr where reportDate between : startDateTime and : endDateTime";

    public List<DailyReportPojo> viewAll(){
        TypedQuery<DailyReportPojo> query = em().createQuery(SELECT_ALL, DailyReportPojo.class);
        return query.getResultList();
    }

    public void insert(DailyReportPojo dailyReportPojo){
        em().persist(dailyReportPojo);
    }

    public List<DailyReportPojo> selectFilteredReport(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        TypedQuery<DailyReportPojo> query = em().createQuery(SELECT_FILTERED, DailyReportPojo.class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        List<DailyReportPojo> dailyReportPojoList = query.getResultList();
        return dailyReportPojoList;
    }

}
