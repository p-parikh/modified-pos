package com.increff.pos.dto;

import com.increff.pos.api.OrderApi;
import com.increff.pos.dto.helper.OrderDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    public List<OrderData> getAllData() throws ApiException{
        List<OrderData> resultSet =new ArrayList<>();
        for(OrderPojo op : orderApi.getAllEntries()) {
            resultSet.add(OrderDtoHelper.convertToOrderData(op));
        }
        return resultSet;
    }

    public OrderData getById(Integer id) throws ApiException{
        return OrderDtoHelper.convertToOrderData(orderApi.getById(id));
    }

    public OrderData getByDatetime(Timestamp datetime) throws ApiException{
        return OrderDtoHelper.convertToOrderData(orderApi.getByDatetime(datetime));
    }

    public void create(OrderPojo orderPojo){
        orderApi.create(orderPojo);
    }
}
