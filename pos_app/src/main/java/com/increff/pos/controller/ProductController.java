package com.increff.pos.controller;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
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
@RequestMapping(path = "/product")
@Log4j
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value="Get list of all products")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        return productDto.getAllData();
    }

    @ApiOperation(value="Get Product by Id")
    @RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
    public ProductData getById(@PathVariable Integer id) throws ApiException {
        return productDto.getById(id);
    }

    @ApiOperation(value="Create Product")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public void create(@RequestBody ProductForm productForm) throws ApiException{
        productDto.create(productForm);
    }

    @ApiOperation(value = "Update Product")
    @RequestMapping(path= "/edit/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm productForm) throws ApiException{
        productDto.update(id, productForm);
    }

    @ApiOperation(value="Upload Product")
    @RequestMapping(path= "/upload", method = RequestMethod.POST)
    public void upload(@RequestBody MultipartFile file) throws Exception {
        productDto.upload(file);
    }
}
