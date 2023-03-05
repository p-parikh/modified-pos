package com.increff.pos.dto.helper;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductDtoHelper {

    @Autowired
    private static ProductApi productApi;

    @Autowired
    private static BrandApi brandApi;
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

    public static ProductPojo normalise(ProductPojo productPojo){
        productPojo.setBarcode(productPojo.getBarcode().trim().toLowerCase());
        productPojo.setName(productPojo.getName().trim().toLowerCase());
        return productPojo;
    }

    public static boolean validateInput(ProductPojo productPojo) throws ApiException {
        if(productPojo.getBarcode() == null)
            throw new ApiException("Please enter valid Barcode");
        if(productPojo.getName() == null)
            throw new ApiException("Please enter valid Name");
        List<Integer> listOfBrandIds = brandApi.selectAllIDs();
        if(!listOfBrandIds.contains(productPojo.getBrandCategory())){
            throw new ApiException("Please enter valid Brand Category ID");
        }
        ProductPojo productWithBarcode = productApi.selectWithBarcode(productPojo.getBarcode());

            if(productWithBarcode != null){
                if(productPojo.getId() != productWithBarcode.getId())
                    throw new ApiException("Provided Product with given barcode already exists ");
            }
        return true;
    }
}
