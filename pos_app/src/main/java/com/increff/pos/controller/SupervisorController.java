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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        System.out.println("Inside upload function");
        log.info("received file");
        log.info(file);
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

    @ApiOperation(value = "Adds a user")
    @RequestMapping(path = "/users/create", method = RequestMethod.POST)
    public void addUser(@RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.addUser(userForm);
    }

    @ApiOperation(value = "Updates a user")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable Integer userId, @RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.updateUser(userId, userForm);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public UserData getUser(@PathVariable Integer userId) throws ApiException {
        return userDto.getById(userId);
    }

    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws ApiException {
        return userDto.getAllUsers();
    }

    @ApiOperation(value= "Get Inventory report")
    @RequestMapping(path= "/inventoryReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getInventoryReport() throws Exception{

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : reportDto.getInventoryReportData()){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    @ApiOperation(value= "Get Brand report")
    @RequestMapping(path= "/brandReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getBrandReport() throws Exception{

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        for(String temp : reportDto.getBrandReportData()){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
    }

    @ApiOperation(value= "Get Sales report")
    @RequestMapping(path="/salesReport/download", method = RequestMethod.GET, produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Object> getSalesReport(@RequestParam String startDateString, @RequestParam String endDateString) throws Exception{
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedStartDate = dateFormat.parse(startDateString);
        Timestamp startDate = new java.sql.Timestamp(parsedStartDate.getTime());
        Date parsedEndDate = dateFormat.parse(endDateString);
        Timestamp endDate = new java.sql.Timestamp(parsedEndDate.getTime());

        for(String temp : reportDto.getSalesReportData(startDate,endDate)){
            printWriter.println(temp);
        }
        printWriter.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.csv\"")
                .body(stringWriter.toString());
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
