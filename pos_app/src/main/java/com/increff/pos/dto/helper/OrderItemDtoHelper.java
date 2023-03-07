package com.increff.pos.dto.helper;

import com.increff.pos.api.ProductApi;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoHelper {
    public static OrderItemPojo convertToOrderItemPojo(OrderItemForm orderItemForm, Integer productId, Integer orderId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
    }

    public static OrderItemData convertToOrderItemData(OrderItemPojo orderItemPojo, ProductPojo productPojo){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setBarcode(productPojo.getBarcode());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setId(orderItemPojo.getId());
        return orderItemData;
    }
}
