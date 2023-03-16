package com.increff.pos.dao;


import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractDao{

    private static final String SELECT_BY_ID = "select o from Orderpojo o where id=:id";

    private static final String SELECT_BY_DATETIME = "select o from Orderpojo o where datetime=:datetime";

    private static final String SELECT_ALL = "select o from Orderpojo o";

    private static final String SELECT_BY_START_AND_END_DATE = "select p from Orderpojo p where updatedAt >=: startDateTime and updatedAt <=: endDateTime";
    public List<OrderPojo> viewAll(){
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public OrderPojo viewById(Integer id){
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_ID, OrderPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    public OrderPojo viewByDatetime(ZonedDateTime datetime){
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_DATETIME, OrderPojo.class);
        query.setParameter("datetime", datetime);
        return getSingleRow(query);
    }

    public void insert(OrderPojo orderPojo){
        em().persist(orderPojo);
    }

    public void update(Integer id, OrderPojo orderPojo) throws ApiException{
        OrderPojo orderPojo_db = viewById(id);

        if(orderPojo_db == null){
            throw new ApiException("Order with provided order id does not exists");
        }

        orderPojo_db.setDatetime(orderPojo.getDatetime());
    }

    public List<OrderPojo> getOrderBetweenStartAndEndDate(ZonedDateTime startDate, ZonedDateTime endDate){
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_START_AND_END_DATE, OrderPojo.class);
        System.out.println("Start Date: "+startDate);
        System.out.println("End Date:" +endDate);
        query.setParameter("startDateTime", startDate);
        query.setParameter("endDateTime", endDate);
        return query.getResultList();
    }

    public void delete(Integer id){
        OrderPojo orderPojo = viewById(id);
        em().remove(orderPojo);
    }
}
