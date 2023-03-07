package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.MultipartConfig;
import java.util.List;

@Api
@MultipartConfig
@RestController
@RequestMapping(path = "/order")
@Log4j
public class OrderItemController {

    @Autowired
    private OrderItemDto orderItemDto;

    @ApiOperation(value = "Get list of all orderItem")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<OrderItemData> getAll() throws ApiException {
        return orderItemDto.getAllData();
    }

    @ApiOperation(value = "Get orderItem by Id")
    @RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
    public OrderItemData getById(@PathVariable Integer id) throws ApiException{
        return orderItemDto.getById(id);
    }

    @ApiOperation(value="create order")
    @RequestMapping(path="/create", method = RequestMethod.POST)
    public Integer create(@RequestBody List<OrderItemForm> orderItemFormList) throws ApiException{
        return orderItemDto.create(orderItemFormList);
    }

    @ApiOperation(value = "Update order")
    @RequestMapping(path= "/edit/{id}", method = RequestMethod.PUT)
    public Integer update(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItemFormList) throws ApiException{
        return orderItemDto.update(id, orderItemFormList);
    }

    @ApiOperation(value = "Get orderItem by order id")
    @RequestMapping(path = "/viewOrder/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getByOrderId(@PathVariable Integer orderId) throws ApiException{
        return orderItemDto.getByOrderId(orderId);
    }
}
