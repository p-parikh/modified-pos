package com.increff.pos.dto.helper;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoHelper {

    public static OrderData convertToOrderData(OrderPojo orderPojo){
        OrderData orderData = new OrderData();
        orderData.setDatetime(orderPojo.getDatetime());
        orderData.setOrderId(orderPojo.getId());
        return orderData;
    }
}
