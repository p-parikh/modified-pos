package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
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
@RequestMapping(path = "/brand")
@Log4j
public class BrandController {

    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value="Get list of all brands")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<BrandData> getAll(){

        return brandDto.getAllData();
    }

    @ApiOperation(value="Get Brand by Id")
    @RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
    public BrandData getById(@PathVariable Integer id) throws ApiException {
        return brandDto.getById(id);
    }

    @ApiOperation(value="Create Brand")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public void create(@RequestBody BrandForm brandForm) throws ApiException{
        brandDto.create(brandForm);
    }

    @ApiOperation(value = "Update Brand")
    @RequestMapping(path= "/edit/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException{
        brandDto.update(id, brandForm);
    }

    @ApiOperation(value="Upload Brand")
    @RequestMapping(path= "/upload", method = RequestMethod.POST)
    public void upload(@RequestBody MultipartFile file) throws Exception {
        System.out.println("Inside upload function");
        log.info("received file");
        log.info(file);
        brandDto.upload(file);
    }
}
