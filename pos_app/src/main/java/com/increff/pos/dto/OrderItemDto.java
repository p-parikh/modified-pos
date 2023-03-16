package com.increff.pos.dto;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private OrderApi orderApi;

    public List<OrderItemData> getAllData() throws ApiException{
        List<OrderItemData> resultSet = new ArrayList<>();

        for(OrderItemPojo oip : orderItemApi.getAllEntries()) {
            ProductPojo productPojo = productApi.getById(oip.getProductId());
            resultSet.add(OrderItemDtoHelper.convertToOrderItemData(oip, productPojo));
        }
        return resultSet;
    }

    public OrderItemData getById(Integer id) throws ApiException{
        OrderItemPojo orderItemPojo = orderItemApi.getById(id);
        ProductPojo productPojo = productApi.getById(orderItemPojo.getProductId());
        return OrderItemDtoHelper.convertToOrderItemData(orderItemPojo, productPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public Integer update(Integer id, List<OrderItemForm> orderItemFormList) throws ApiException{
        ValidationUtil.checkValid(orderItemFormList);
        List<OrderItemPojo> orderItemPojo_db = orderItemApi.selectByOrderId(id);

        for(OrderItemPojo temp : orderItemPojo_db){
            InventoryPojo inventoryPojo = inventoryApi.getById(temp.getProductId());
            inventoryPojo.setQty(temp.getQuantity() + inventoryPojo.getQty());
            inventoryApi.update(temp.getProductId(), inventoryPojo);
            orderItemApi.delete(temp.getId());
        }

        orderApi.delete(id);
       return create(orderItemFormList);
    }

    @Transactional(rollbackFor = ApiException.class)
    public Integer create(List<OrderItemForm> orderItemFormList) throws ApiException {
        for(OrderItemForm orderItemForm : orderItemFormList){
            ValidationUtil.checkValid(orderItemForm);
        }
        ZonedDateTime datetime = ZonedDateTime.now(ZoneId.systemDefault());
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(datetime);
        orderApi.create(orderPojo);

        OrderPojo orderPojo_db = orderApi.getByDatetime(datetime);
        for(OrderItemForm orderItemForm : orderItemFormList){
            ProductPojo productPojo = productApi.selectWithBarcode(orderItemForm.getBarcode());

            if (productPojo == null) {
                throw new ApiException("Product with provided barcode does not exists");
            }

            InventoryPojo inventoryPojo = inventoryApi.getById(productPojo.getId());

            if(inventoryPojo == null){
                throw new ApiException("Inventory for provided product does not exists");
            }

            if (inventoryPojo.getQty() < orderItemForm.getQuantity()) {
                throw new ApiException("Demanded quantity of required product can not be fulfilled");
            }

            inventoryPojo.setQty(inventoryPojo.getQty() - orderItemForm.getQuantity());
            inventoryApi.update(productPojo.getId(), inventoryPojo);
            orderItemApi.create(OrderItemDtoHelper.convertToOrderItemPojo(orderItemForm, productPojo.getId(), orderPojo_db.getId()));
        }
        return orderPojo_db.getId();
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
