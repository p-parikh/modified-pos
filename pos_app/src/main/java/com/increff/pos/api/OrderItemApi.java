package com.increff.pos.api;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderItemApi {

    @Autowired
    private OrderItemDao orderItemDao;

    public List<OrderItemPojo> getAllEntries(){
        return orderItemDao.viewAll();
    }

    public OrderItemPojo getById(Integer id) throws ApiException{
        OrderItemPojo orderItemPojo = orderItemDao.viewById(id);

        if(orderItemPojo == null){
            throw new ApiException("Order does not exists with given id");
        }

        return orderItemPojo;
    }

    public void create(OrderItemPojo orderItemPojo){
        orderItemDao.insert(orderItemPojo);
    }

    public void update(Integer id, OrderItemPojo orderItemPojo) throws ApiException{
        orderItemDao.update(id, orderItemPojo);
    }

    public OrderItemPojo selectByOrderAndProductId(Integer orderId, Integer productId){
        return orderItemDao.selectWithOrderAndProductId(orderId,productId);
    }

    public List<OrderItemPojo> selectByOrderId(Integer orderId){
        return orderItemDao.selectWithOrderId(orderId);
    }

    public void delete(Integer id) throws ApiException{
        orderItemDao.delete(id);
    }
}
