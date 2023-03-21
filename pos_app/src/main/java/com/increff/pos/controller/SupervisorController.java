package com.increff.pos.controller;

import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api
@RestController
@Log4j
@MultipartConfig
@RequestMapping(path = "/api/supervisor")
public class SupervisorController {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ReportDto reportDto;
    @Autowired
    private DailyReportDto dailyReportDto;

    @Autowired
    private UserDto userDto;

    @ApiOperation(value="Create Brand")
    @RequestMapping(path = "/brand/create", method = RequestMethod.POST)
    public void createBrand(@RequestBody BrandForm brandForm) throws ApiException {
        brandDto.create(brandForm);
    }

    @ApiOperation(value = "Update Brand")
    @RequestMapping(path= "/brand/edit/{id}", method = RequestMethod.PUT)
    public void updateBrand(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException{
        brandDto.update(id, brandForm);
    }

    @ApiOperation(value="Upload Brand")
    @RequestMapping(path= "/brand/upload", method = RequestMethod.POST)
    public void uploadBrand(@RequestBody MultipartFile file) throws Exception {
        brandDto.upload(file);
    }

    @ApiOperation(value="Create Product")
    @RequestMapping(path = "/product/create", method = RequestMethod.POST)
    public void createProduct(@RequestBody ProductForm productForm) throws ApiException{
        productDto.create(productForm);
    }

    @ApiOperation(value = "Update Product")
    @RequestMapping(path= "/product/edit/{id}", method = RequestMethod.PUT)
    public void updateProduct(@PathVariable Integer id, @RequestBody ProductForm productForm) throws ApiException{
        productDto.update(id, productForm);
    }

    @ApiOperation(value="Upload Product")
    @RequestMapping(path= "/product/upload", method = RequestMethod.POST)
    public void uploadProduct(@RequestBody MultipartFile file) throws Exception {
        productDto.upload(file);
    }

    @ApiOperation(value="create Inventory")
    @RequestMapping(path="/inventory/create", method = RequestMethod.POST)
    public void createInventory(@RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.create(inventoryForm);
    }

    @ApiOperation(value = "Update Inventory")
    @RequestMapping(path= "/inventory/edit/{id}", method = RequestMethod.PUT)
    public void updateInventory(@PathVariable Integer id, @RequestBody InventoryForm inventoryForm) throws ApiException{
        inventoryDto.update(id, inventoryForm);
    }

    @ApiOperation(value="Upload Inventory")
    @RequestMapping(path= "/inventory/upload", method = RequestMethod.POST)
    public void uploadInventory(@RequestBody MultipartFile file) throws Exception {
        inventoryDto.upload(file);
    }

    @ApiOperation(value = "Create user")
    @RequestMapping(path = "/users/create", method = RequestMethod.POST)
    public void addUser(@RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.addUser(userForm);
    }

    @ApiOperation(value = "Update user")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable Integer userId, @RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.updateUser(userId, userForm);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws ApiException {
        return userDto.getAllUsers();
    }

    @ApiOperation(value= "Get Inventory report")
    @RequestMapping(path= "/inventoryReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public void getInventoryReport(HttpServletResponse httpServletResponse) throws Exception{
         reportDto.getInventoryReportData(httpServletResponse);
    }

    @ApiOperation(value= "Get Brand report")
    @RequestMapping(path= "/brandReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public void getBrandReport(HttpServletResponse httpServletResponse) throws Exception{
         reportDto.getBrandReportData(httpServletResponse);
    }

    @ApiOperation(value= "Get Sales report")
    @RequestMapping(path="/salesReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public void getSalesReport(HttpServletResponse httpServletResponse,@RequestParam String startDateString, @RequestParam String endDateString) throws Exception{
         reportDto.getSalesReportData(startDateString, endDateString, httpServletResponse);
    }

    @ApiOperation(value = "Get Daily report")
    @RequestMapping(path="/daily-report", method = RequestMethod.GET)
    public List<DailyReportData> getDailyReport() throws ApiException{
        return dailyReportDto.getAll();
    }

    @ApiOperation(value = "Gets daily report with filter")
    @RequestMapping(path = "/daily-report", method = RequestMethod.POST)
    public List<DailyReportData> getFilteredDailyReport(@RequestBody DailyReportForm dailyReportForm) throws ApiException {
        return dailyReportDto.getFilteredReport(dailyReportForm);
    }
}
