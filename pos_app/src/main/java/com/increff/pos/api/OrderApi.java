package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    public List<OrderPojo> getAllEntries(){
        return orderDao.viewAll();
    }

    public OrderPojo getById(Integer id) throws ApiException {
        OrderPojo orderPojo = orderDao.viewById(id);
        if(orderPojo == null){
            throw new ApiException("Order with given id does not exists");
        }

        return orderPojo;
    }

    public OrderPojo getByDatetime(ZonedDateTime datetime) throws ApiException {

        OrderPojo orderPojo = orderDao.viewByDatetime(datetime);
        if(orderPojo == null){
            throw new ApiException("Order with given datetime does not exists");
        }

        return orderPojo;
    }

    public void create(OrderPojo orderPojo){
        orderDao.insert(orderPojo);
    }

    public void update(Integer id, OrderPojo orderPojo) throws ApiException{
        orderDao.update(id,orderPojo);
    }

    public List<OrderPojo> getOrderBetweenStartEndDate(ZonedDateTime startDate, ZonedDateTime endDate){
        System.out.println("inside get order between start end date");
        List<OrderPojo>  orderPojoList= orderDao.getOrderBetweenStartAndEndDate(startDate,endDate);
        int count = 0;
        for(OrderPojo orderPojo : orderPojoList){
            count++;
        }
        System.out.println("orderApi count: " +count);
        return orderPojoList;
    }

    public void delete(Integer id){
        orderDao.delete(id);
    }
}
