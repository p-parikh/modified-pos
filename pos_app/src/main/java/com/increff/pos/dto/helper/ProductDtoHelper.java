package com.increff.pos.dto.helper;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


public class ProductDtoHelper {

    public static ProductPojo convertToProductPojo(ProductForm productForm, Integer brandCategoryId){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setBrandCategory(brandCategoryId);
        return productPojo;
    }

    public static ProductData convertToProductData(ProductPojo productPojo, BrandPojo brandPojo){
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        return productData;
    }

    public static ProductForm normalise(ProductForm productForm){
        productForm.setBarcode(productForm.getBarcode().trim().toLowerCase());
        productForm.setName(productForm.getName().trim().toLowerCase());
        return productForm;
    }

}
