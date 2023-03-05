package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
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

    public OrderPojo getByDatetime(Timestamp datetime) throws ApiException {

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

}
