package com.increff.pos.dto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import io.swagger.annotations.Api;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddBrand() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Test Brand");
        brandForm.setCategory("teSt catEgOrY");
        brandDto.create(brandForm);
        BrandData brandData = brandDto.getAllData().get(0);
        assertEquals("test brand", brandData.getBrand());
        assertEquals("test category", brandData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testBrandCategoryUniquenessOnAdd() throws ApiException {
        BrandForm brandForm = new BrandForm();
        String brand = "brand";
        String category = "category";
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        brandDto.create(brandForm);
        try{
            brandDto.create(brandForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Provided Brand Category Pair already exists", e.getMessage());
            throw new ApiException("Provided Brand Category Pair already exists");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyBrandOnAdd() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("");
        brandForm.setCategory("category");
        try{
            brandDto.create(brandForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyCategoryOnAdd() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand");
        brandForm.setCategory("");
        try{
            brandDto.create(brandForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }


    @Test
    public void testGetAll() throws ApiException {
        BrandForm brandForm = new BrandForm();
        for (Integer i = 0; i < 10; i++) {
            brandForm.setBrand("Brand_" + i);
            brandForm.setCategory("Category_" + i);
            brandDto.create(brandForm);
        }
        List<BrandData> brandDataList = brandDto.getAllData();
        assertEquals(10, brandDataList.size());
        for (Integer i = 0; i < 10; i++) {
            assertEquals("brand_" + i, brandDataList.get(i).getBrand());
            assertEquals("category_" + i, brandDataList.get(i).getCategory());
        }
    }


    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Brand");
        brandForm.setCategory("Category");
        Integer brandId = brandDto.create(brandForm);
        brandForm.setBrand("new Brand");
        brandForm.setCategory("new Category");
        brandDto.update(brandId, brandForm);
        brandForm = brandDto.getAllData().get(0);
        assertEquals("new brand", brandForm.getBrand());
        assertEquals("new category", brandForm.getCategory());
    }
}
