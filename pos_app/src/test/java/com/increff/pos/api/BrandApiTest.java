package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandApiTest extends AbstractUnitTest{

    @Autowired
    private BrandApi brandApi;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandApi.create(brandPojo);
        BrandPojo brandPojo_db = brandApi.getById(brandId);
        matchPojo(0, brandPojo_db);
    }


    @Test
    public void testGetAll() throws ApiException {
        for(int i = 0; i < 5; i++){
            BrandPojo brandPojo = createBrandPojo(i);
            brandApi.create(brandPojo);
        }
        List<BrandPojo> brandPojoList = brandApi.getAllEntries();
        assertEquals(5, brandPojoList.size());
        for(int i = 0; i < 5; i++){
            matchPojo(i, brandPojoList.get(i));
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandApi.create(brandPojo);
        BrandPojo newBrandPojo = createBrandPojo(1);
        brandApi.update(brandId, newBrandPojo);
        matchPojo(1, newBrandPojo);
    }

    @Test
    public void testFindBrandCategoryId() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        Integer brandId = brandApi.create(brandPojo);
        BrandPojo brandPojo_db= brandApi.selectWithBrandAndCategory("brand0", "category0");
        assertEquals(brandId, brandPojo_db.getId());
    }

    @Test
    public void testFindBrandCategoryIdForIncorrectPair() throws ApiException {
        BrandPojo brandPojo = createBrandPojo(0);
        brandApi.create(brandPojo);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("brand0, category1 pair does not exist");
        brandApi.selectWithBrandAndCategory("brand0", "category1");
    }


    private void matchPojo(Integer id, BrandPojo brandPojo){
        assertEquals("brand" + id, brandPojo.getBrand());
        assertEquals("category" + id, brandPojo.getCategory());
    }

    private BrandPojo createBrandPojo(Integer id){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        return brandPojo;
    }

}
