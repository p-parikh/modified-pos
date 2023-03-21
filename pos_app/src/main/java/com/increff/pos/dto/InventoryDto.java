package com.increff.pos.dto;


import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FileConversionUtil;
import com.increff.pos.util.TsvUtil;
import com.increff.pos.util.ValidationUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private TsvUtil tsvUtil;

    public List<InventoryData> getAllData() throws ApiException {
        List<InventoryData> resultSet = new ArrayList<>();
        for(InventoryPojo ip : inventoryApi.getAllEntries()){
            ProductPojo productPojo = productApi.getById(ip.getProductId());
            resultSet.add(InventoryDtoHelper.convertToInventoryData(ip, productPojo));
        }
        return resultSet;
    }

    public void update(Integer id, InventoryForm inventoryForm) throws ApiException{
        ValidationUtil.checkValid(inventoryForm);
        ProductPojo productPojo = productApi.selectWithBarcode(inventoryForm.getBarcode());
        if(productPojo == null){
            throw new ApiException("Product with provided barcode does not exists");
        }
        InventoryPojo inventoryPojo = InventoryDtoHelper.convertToInventoryPojo(inventoryForm, productPojo.getId());
        inventoryApi.update(id, inventoryPojo);
    }

    public Integer create(InventoryForm inventoryForm) throws ApiException {
        ValidationUtil.checkValid(inventoryForm);
        ProductPojo productPojo = productApi.selectWithBarcode(inventoryForm.getBarcode());
        if(productPojo == null){
            throw new ApiException("Product with provided barcode does not exists");
        }
        InventoryPojo inventoryPojo = InventoryDtoHelper.convertToInventoryPojo(inventoryForm, productPojo.getId());
        inventoryApi.create(inventoryPojo);
        return inventoryPojo.getProductId();
    }

    public void upload(MultipartFile inventoryTsv) throws Exception{
        File convertedTsv = FileConversionUtil.convert(inventoryTsv);
        List<InventoryForm> uploadList = tsvUtil.convert(convertedTsv, InventoryForm.class);
        ValidationUtil.checkValidMultiple(uploadList);
        validateProduct(uploadList);
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(InventoryForm inventoryForm : uploadList){
            ProductPojo productPojo = productApi.selectWithBarcode(inventoryForm.getBarcode());
            InventoryPojo inventoryPojo = InventoryDtoHelper.convertToInventoryPojo(inventoryForm, productPojo.getId());
            inventoryPojoList.add(inventoryPojo);
        }
        inventoryApi.createMultiple(inventoryPojoList);
    }

    private void validateProduct(List<InventoryForm> uploadList) throws ApiException{
        List<String> errorList = new ArrayList<>();
        for(InventoryForm inventoryForm : uploadList){
            ProductPojo productPojo = productApi.selectWithBarcode(inventoryForm.getBarcode());
            if(productPojo == null){
                String error = new String("Error: Product with provided barcode: "
                        +inventoryForm.getBarcode() + " does not exists");
                errorList.add(error);
            }
        }
        if(errorList.isEmpty())
            return;
        throw new ApiException("Barcode Incorrect", errorList);
    }
}
