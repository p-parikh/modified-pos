package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private OrderItemDto orderItemDto;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);

        List<OrderItemData> orderItemDataList = orderItemDto.getByOrderId(orderId);

        matchData(0, orderItemDataList);
    }

    @Test(expected = ApiException.class)
    public void testInvalidBarcodeOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setBarcode("barcode9");
        try{
            orderItemDto.create(orderItemFormList);
        }
        catch (ApiException e){
            Assert.assertEquals("Product with provided barcode does not exists", e.getMessage());
            throw new ApiException("Product with provided barcode does not exists");
        }

    }

    @Test(expected = ApiException.class)
    public void testEmptyBarcodeOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setBarcode("");
        try {
            orderItemDto.create(orderItemFormList);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyQuantityOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setQuantity(null);
        try {
            orderItemDto.create(orderItemFormList);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }


    @Test(expected = ApiException.class)
    public void testEmptySellingPriceOnAddOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        orderItemFormList.get(0).setSellingPrice(null);
        orderItemDto.create(orderItemFormList);
    }

    @Test
    public void testGetAllOrders() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 5; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            orderItemDto.create(orderItemFormList);
        }
        List<OrderItemData> orderDataList = orderItemDto.getAllData();
        assertEquals(10, orderDataList.size());
    }

    @Test
    public void testGetOrderItemsOrderId() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);
        List<OrderItemData> orderItemDataList = orderItemDto.getByOrderId(orderId);

        matchData(0, orderItemDataList);

    }

    @Test
    public void testUpdateOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);

        List<OrderItemForm> newOrderItemFormList = createOrder(1);
        Integer updatedOrderId = orderItemDto.update(orderId, newOrderItemFormList);

        List<OrderItemData> orderItemDataList = orderItemDto.getByOrderId(updatedOrderId);
        matchData(1, orderItemDataList);
        List<InventoryData> inventoryDataList = inventoryDto.getAllData();
        assertEquals((Integer) 400, inventoryDataList.get(0).getQty());
        assertEquals((Integer) 400, inventoryDataList.get(1).getQty());
    }

    @Test(expected = ApiException.class)
    public void testNegativeQuantityOnUpdateOrder() throws ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);

        List<OrderItemForm> newOrderItemFormList = createOrder(1);
        newOrderItemFormList.get(0).setQuantity(-1);
        try{
            orderItemDto.update(orderId, newOrderItemFormList);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }

    }

    private void matchData(Integer id, List<OrderItemData> orderItemDataList) throws ApiException {
        assertEquals(2, orderItemDataList.size());

        OrderItemData orderItemData = orderItemDataList.get(0);
        assertEquals("barcode" + id, orderItemData.getBarcode());
        assertEquals((Integer) 200, orderItemData.getQuantity());
        assertEquals((Double) 98.5, orderItemData.getSellingPrice());

        orderItemData = orderItemDataList.get(1);
        assertEquals(id + "barcode", orderItemData.getBarcode());
        assertEquals((Integer) 200, orderItemData.getQuantity());
        assertEquals((Double) 198.5, orderItemData.getSellingPrice());

        List<InventoryData> inventoryDataList = inventoryDto.getAllData();
        assertEquals((Integer) 200, inventoryDataList.get(id * 2).getQty());
        assertEquals((Integer) 200, inventoryDataList.get(id * 2 + 1).getQty());

    }
}
