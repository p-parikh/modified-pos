package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.util.List;

@Api
@MultipartConfig
@RestController
@RequestMapping(path = "/inventory")
@Log4j
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Get list of all inventory")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAllData();
    }

    @ApiOperation(value = "Get Inventory by Product Id")
    @RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
    public InventoryData getById(@PathVariable Integer id) throws ApiException{
        return inventoryDto.getById(id);
    }

    @ApiOperation(value="create Inventory")
    @RequestMapping(path="/create", method = RequestMethod.POST)
    public void create(@RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.create(inventoryForm);
    }

    @ApiOperation(value = "Update Inventory")
    @RequestMapping(path= "/edit/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.update(id, inventoryForm);
    }

    @ApiOperation(value="Upload Inventory")
    @RequestMapping(path= "/upload", method = RequestMethod.POST)
    public void upload(@RequestBody MultipartFile file) throws Exception {
        inventoryDto.upload(file);
    }
}
