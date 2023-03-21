package com.increff.pos.dao;

import com.increff.pos.api.DailyReportApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{

    private static final String SELECT_ALL = "select dr from Dailyreport dr";
    private static final String SELECT_FILTERED = "select dr from Dailyreport dr where reportDate between : startDateTime and : endDateTime";

    private static final String SELECT_BY_DATE = "select dr from Dailyreport dr where reportDate =:reportDate";
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

    public DailyReportPojo getByDate(ZonedDateTime reportDate){
        TypedQuery<DailyReportPojo> query = em().createQuery(SELECT_BY_DATE, DailyReportPojo.class);
        query.setParameter("reportDate", reportDate);
        return getSingleRow(query);
    }

    public void update(ZonedDateTime reportDate, DailyReportPojo dailyReportPojo){
        DailyReportPojo dailyReportPojo_db = getByDate(reportDate);
        dailyReportPojo_db.setTotalItems(dailyReportPojo.getTotalItems());
        dailyReportPojo_db.setTotalOrders(dailyReportPojo.getTotalOrders());
        dailyReportPojo_db.setTotalRevenue(dailyReportPojo.getTotalRevenue());
    }

}
