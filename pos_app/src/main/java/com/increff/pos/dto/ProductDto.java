package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
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
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private TsvUtil tsvUtil;

    public List<ProductData> getAllData() throws ApiException{
        List<ProductData> resultSet = new ArrayList<>();
        for(ProductPojo productPojo : productApi.getAllEntries()){
            BrandPojo brandPojo = brandApi.getById(productPojo.getBrandCategory());
            resultSet.add(ProductDtoHelper.convertToProductData(productPojo, brandPojo));
        }
        return resultSet;
    }

    public void update(Integer id, ProductForm productForm) throws ApiException{
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        productApi.update(id, ProductDtoHelper.normalise(productPojo));
    }

    public Integer create(ProductForm productForm) throws ApiException{
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        productApi.create(ProductDtoHelper.normalise(productPojo));
        return productPojo.getId();
    }

    public void upload(MultipartFile productTsv) throws Exception {
        //TODO mock multipart spring package for test
        File convertedTsv = FileConversionUtil.convert(productTsv);
        List<ProductForm> uploadList = tsvUtil.convert(convertedTsv, ProductForm.class);
        ValidationUtil.checkValid(uploadList);
        validateBrandCategory(uploadList);
        for(ProductForm productForm : uploadList) {
            BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(), productForm.getCategory());
            ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
            productApi.create(productPojo);
        }
    }

    private void validateBrandCategory(List<ProductForm> uploadList) throws ApiException{
        List<String> errorList = new ArrayList<>();
        for(ProductForm productForm : uploadList){
            BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(), productForm.getCategory());
            if(brandPojo == null){
                String error = new String("Error: Brand: " +productForm.getBrand() +
                        " and category: " + productForm.getCategory() + " does not exists");
                errorList.add(error);
            }
        }
        if(errorList.isEmpty())
            return;
        throw new ApiException("Brand Category Incorrect", errorList);
    }
}
