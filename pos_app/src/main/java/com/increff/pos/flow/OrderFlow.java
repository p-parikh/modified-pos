package com.increff.pos.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderFlow {
    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderItemApi orderItemApi;

    public Integer create(List<OrderItemForm> orderItemFormList) throws ApiException{
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
    public Integer update(Integer id, List<OrderItemForm> orderItemFormList) throws ApiException{
        List<OrderItemPojo> orderItemPojo_db = orderItemApi.selectByOrderId(id);
        for(OrderItemPojo orderItemPojo : orderItemPojo_db){
            InventoryPojo inventoryPojo = inventoryApi.getById(orderItemPojo.getProductId());
            inventoryPojo.setQty(orderItemPojo.getQuantity() + inventoryPojo.getQty());
            inventoryApi.update(orderItemPojo.getProductId(), inventoryPojo);
            orderItemApi.delete(orderItemPojo.getId());
        }
        orderApi.delete(id);
        return create(orderItemFormList);
    }
}
