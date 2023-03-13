package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemApiTest extends AbstractUnitTest{

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private OrderApi orderApi;
    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;
    @Test
    public void testCreateOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Timestamp datetime = new Timestamp(System.currentTimeMillis());
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(datetime);
        orderApi.create(orderPojo);
        OrderPojo orderPojo_db = orderApi.getByDatetime(datetime);

        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            orderItemPojo.setOrderId(orderPojo_db.getId());
            orderItemApi.create(orderItemPojo);
        }
        matchOrder(0, orderItemApi.selectByOrderId(orderPojo_db.getId()));
    }

    @Test
    public void testUpdateOrder() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Timestamp datetime = new Timestamp(System.currentTimeMillis());
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(datetime);
        orderApi.create(orderPojo);
        OrderPojo orderPojo_db = orderApi.getByDatetime(datetime);

        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            orderItemPojo.setOrderId(orderPojo_db.getId());
            orderItemApi.create(orderItemPojo);
        }
        List<OrderItemPojo> orderItemPojoList_db = orderItemApi.selectByOrderId(orderPojo_db.getId());
        for(OrderItemPojo temp : orderItemPojoList_db){
            InventoryPojo inventoryPojo = inventoryApi.getById(temp.getProductId());
            inventoryPojo.setQty(temp.getQuantity() + inventoryPojo.getQty());
            inventoryApi.update(temp.getProductId(), inventoryPojo);
            orderItemApi.delete(temp.getId());
        }

        List<OrderItemPojo> newOrderItemPojoList = createOrderItemPojoList(1);
        for(OrderItemPojo orderItemPojo: newOrderItemPojoList){
            orderItemPojo.setOrderId(orderPojo_db.getId());
            orderItemApi.create(orderItemPojo);
        }
        matchOrder(1, orderItemApi.selectByOrderId(orderPojo_db.getId()));
    }

    @Test
    public void testGetAllOrders() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        Timestamp datetime = new Timestamp(System.currentTimeMillis());
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(datetime);
        orderApi.create(orderPojo);
        OrderPojo orderPojo_db = orderApi.getByDatetime(datetime);

        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            orderItemPojo.setOrderId(orderPojo_db.getId());
            orderItemApi.create(orderItemPojo);
        }
        assertEquals(orderPojo_db.getId(), orderItemApi.getAllEntries().get(0).getOrderId());
    }

    @Test(expected = ApiException.class)
    public void testGetOrderPojoWithIncorrectId() throws ApiException {
        orderApi.getById(-1);
    }

    @Test(expected = ApiException.class)
    public void testDeleteOrderItemWithIncorrectId() throws ApiException {
        orderItemApi.delete(-1);
    }


    private void matchOrder(Integer id, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Integer productId1 = productApi.selectWithBarcode("barcode" + id).getId();
        assertEquals(productId1, orderItemPojoList.get(0).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(0).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(0).getSellingPrice());

        Integer productId2 = productApi.selectWithBarcode(id + "barcode").getId();
        assertEquals(productId2, orderItemPojoList.get(1).getProductId());
        assertEquals((Integer) 10, orderItemPojoList.get(1).getQuantity());
        assertEquals((Double) 20.0, orderItemPojoList.get(1).getSellingPrice());
    }
}
