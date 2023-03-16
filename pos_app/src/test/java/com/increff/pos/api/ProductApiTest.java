package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductApiTest extends AbstractUnitTest {
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ProductApi productApi;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productApi.create(productPojo);
        ProductPojo productPojoInDb = productApi.getById(productId);
        matchPojo(0, productPojoInDb);
    }


    @Test(expected = ApiException.class)
    public void testGetOnWrongId() throws ApiException {
       try {
           productApi.getById(0);
       }
       catch (ApiException e){
           Assert.assertEquals("Product does not exists with given id",e.getMessage());
           throw new ApiException("Product does not exists with given id");
       }
    }

    @Test
    public void testGetAll() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductPojo productPojo = createProductPojo(i);
            productApi.create(productPojo);
        }
        productPojoList = productApi.getAllEntries();
        assertEquals(3, productPojoList.size());
        for (int i = 0; i < 3; i++) {
            matchPojo(i, productPojoList.get(i));
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productApi.create(productPojo);
        productPojo.setName("new product");
        productPojo.setMrp(1.0);
        productApi.update(productId, productPojo);
        assertEquals("new product", productPojo.getName());
        assertEquals((Double) 1.0, productPojo.getMrp());
    }

    @Test
    public void testGetIdFromBarcode() throws ApiException {
        ProductPojo productPojo = createProductPojo(0);
        Integer productId = productApi.create(productPojo);
        assertEquals(productId, productApi.selectWithBarcode("barcode0").getId());
    }

    private void matchPojo(Integer id, ProductPojo productPojo) throws ApiException {
        assertEquals("barcode" + id, productPojo.getBarcode());
        assertEquals("product" + id, productPojo.getName());
        assertEquals((Double) 100.0, productPojo.getMrp());
    }

    private ProductPojo createProductPojo(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        brandApi.create(brandPojo);
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode" + id);
        productPojo.setBrandCategory(brandApi.selectWithBrandAndCategory(brandPojo.getBrand(), brandPojo.getCategory()).getId());
        productPojo.setName("product" + id);
        productPojo.setMrp(100.0);
        return productPojo;
    }

}
