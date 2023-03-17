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

    public void create(OrderItemPojo orderItemPojo){
        orderItemDao.insert(orderItemPojo);
    }

    public void update(Integer id, OrderItemPojo orderItemPojo) throws ApiException{
        orderItemDao.update(id, orderItemPojo);
    }

    public List<OrderItemPojo> selectByOrderId(Integer orderId){
        return orderItemDao.selectWithOrderId(orderId);
    }

    public void delete(Integer id) throws ApiException{
        orderItemDao.delete(id);
    }
}
