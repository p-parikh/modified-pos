package com.increff.pos.dto;


import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.InventoryDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FileConversionUtil;
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


    public List<InventoryData> getAllData() throws ApiException {
        List<InventoryData> resultSet = new ArrayList<>();

        for(InventoryPojo ip : inventoryApi.getAllEntries()){
            ProductPojo productPojo = productApi.getById(ip.getProductId());
            resultSet.add(InventoryDtoHelper.convertToInventoryData(ip, productPojo));
        }
        return resultSet;
    }

    public InventoryData getById(Integer id) throws ApiException{
        InventoryPojo inventoryPojo = inventoryApi.getById(id);
        ProductPojo productPojo = productApi.getById(inventoryPojo.getProductId());
        return InventoryDtoHelper.convertToInventoryData(inventoryPojo,productPojo);
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
        if(validateInput(inventoryPojo)){
            inventoryApi.create(inventoryPojo);
        }

        return inventoryPojo.getProductId();
    }

    public void upload(MultipartFile inventoryTsv) throws Exception{
        File convertedTsv = FileConversionUtil.convert(inventoryTsv);
        String fileExtension = FilenameUtils.getExtension(convertedTsv.toString());
        if(!fileExtension.equals("tsv")){
            throw new ApiException("Input file is not a valid TSV file");
        }
        TsvToJson tsvParse = new TsvToJson();
        List<HashMap<String, Object>> values = tsvParse.tsvToJson(convertedTsv);
        String barcode = "barcode";
        String qty = "qty";

        for(HashMap<String, Object> line : values){
            InventoryForm inventoryForm = new InventoryForm();
            inventoryForm.setBarcode((String) line.get(barcode));
            inventoryForm.setQty(Long.parseLong((String) line.get(qty)));
            create(inventoryForm);
        }
    }

    public boolean validateInput(InventoryPojo inventoryPojo) throws ApiException{
        List<Integer> allProductId = inventoryApi.getAllId();
        if(allProductId.contains(inventoryPojo.getProductId())){
            throw new ApiException("Inventory for given product already exists");
        }
        return true;
    }
}
