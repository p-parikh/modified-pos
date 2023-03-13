package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/operator")
public class OperatorController {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private PdfGeneratorDto pdfGeneratorDto;

    @ApiOperation(value="Get list of all brands")
    @RequestMapping(path = "/brand/viewAll", method = RequestMethod.GET)
    public List<BrandData> getAllBrands(){
        return brandDto.getAllData();
    }

    @ApiOperation(value="Get list of all products")
    @RequestMapping(path = "/product/viewAll", method = RequestMethod.GET)
    public List<ProductData> getAllProducts() throws ApiException {
        return productDto.getAllData();
    }

    @ApiOperation(value = "Get list of all inventory")
    @RequestMapping(path = "/inventory/viewAll", method = RequestMethod.GET)
    public List<InventoryData> getAllInventory() throws ApiException {
        return inventoryDto.getAllData();
    }

    @ApiOperation(value="create order")
    @RequestMapping(path="/order/create", method = RequestMethod.POST)
    public Integer createOrder(@RequestBody List<OrderItemForm> orderItemFormList) throws ApiException{
        return orderItemDto.create(orderItemFormList);
    }

    @ApiOperation(value = "Update order")
    @RequestMapping(path= "/order/edit/{id}", method = RequestMethod.PUT)
    public Integer updateOrder(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItemFormList) throws ApiException{
        return orderItemDto.update(id, orderItemFormList);
    }

    @ApiOperation(value = "Get orderItem by order id")
    @RequestMapping(path = "/order/viewOrder/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getOrderByOrderId(@PathVariable Integer orderId) throws ApiException{
        return orderItemDto.getByOrderId(orderId);
    }

    @ApiOperation(value = "Get list of all orderItem")
    @RequestMapping(path = "/order/viewAll", method = RequestMethod.GET)
    public List<OrderItemData> getAll() throws ApiException {
        return orderItemDto.getAllData();
    }

    @ApiOperation(value = "generate invoice")
    @RequestMapping(path = "/order/generate-invoice/{orderId}", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable Integer orderId) throws ApiException{
        pdfGeneratorDto.generatePdf(orderId);
    }

    @ApiOperation(value = "download invoice pdf")
    @RequestMapping(path = "/order/download-invoice/{orderId}", method = RequestMethod.GET)
    public String downloadInvoice(@PathVariable Integer orderId) throws IOException {
        return pdfGeneratorDto.downloadInvoice(orderId);
    }
}
