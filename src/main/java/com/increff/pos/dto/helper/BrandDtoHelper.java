package com.increff.pos.dto.helper;

import com.increff.pos.api.BrandApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BrandDtoHelper {

    @Autowired
    private static BrandApi brandApi;

    public static BrandPojo convertToBrandPojo(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setCategory(brandForm.getCategory());
        brandPojo.setBrand(brandForm.getBrand());
        return brandPojo;
    }

    public static BrandData convertToBrandData(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    public static BrandPojo normalise(BrandPojo brandPojo) {
        brandPojo.setBrand(brandPojo.getBrand().trim().toLowerCase());
        brandPojo.setCategory(brandPojo.getCategory().trim().toLowerCase());
        return brandPojo;
    }

    public static boolean validateInput(BrandPojo brandPojo) throws ApiException {
        if (brandPojo.getBrand() == null)
            throw new ApiException("Please enter valid Brand");
        if (brandPojo.getCategory() == null)
            throw new ApiException("Please enter valid Category");
        BrandPojo brandPojoWithBrandCategoryCombo = brandApi
                .selectWithBrandAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
            if (brandPojoWithBrandCategoryCombo != null) {
                if (brandPojoWithBrandCategoryCombo.getId() != brandPojo.getId())
                    throw new ApiException("Provided Brand Category Pair already exists");
            }
        return true;
    }

}
