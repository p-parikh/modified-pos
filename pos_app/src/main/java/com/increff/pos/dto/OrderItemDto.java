package com.increff.pos.dto;

import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private OrderFlow orderFlow;

    public List<OrderItemData> getAllData() throws ApiException{
        List<OrderItemData> resultSet = new ArrayList<>();
        for(OrderItemPojo oip : orderItemApi.getAllEntries()) {
            ProductPojo productPojo = productApi.getById(oip.getProductId());
            resultSet.add(OrderItemDtoHelper.convertToOrderItemData(oip, productPojo));
        }
        return resultSet;
    }

    public Integer update(Integer id, List<OrderItemForm> orderItemFormList) throws ApiException{
        ValidationUtil.checkValid(orderItemFormList);
        return orderFlow.update(id, orderItemFormList);
    }

    public Integer create(List<OrderItemForm> orderItemFormList) throws ApiException {
        ValidationUtil.checkValid(orderItemFormList);
        return orderFlow.create(orderItemFormList);
    }

    public List<OrderItemData> getByOrderId(Integer orderId) throws ApiException{
        List<OrderItemPojo> resultData = orderItemApi.selectByOrderId(orderId);
        if(resultData.isEmpty()){
            throw new ApiException("Order with provided id does not exist");
        }
        List<OrderItemData> resultSet = new ArrayList<>();
        for(OrderItemPojo oip : resultData) {
            ProductPojo productPojo = productApi.getById(oip.getProductId());
            resultSet.add(OrderItemDtoHelper.convertToOrderItemData(oip, productPojo));
        }
        return resultSet;
    }
}
