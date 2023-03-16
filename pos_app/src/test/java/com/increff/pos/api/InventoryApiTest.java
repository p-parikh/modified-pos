package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class InventoryApiTest  extends AbstractUnitTest {
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        inventoryApi.create(inventoryPojo);
        matchPojo(0, inventoryApi.getAllEntries().get(0));
    }

    @Test
    public void testAddOnExistingInventory() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        inventoryApi.create(inventoryPojo);
        inventoryPojo.setQty(800);
        inventoryApi.create(inventoryPojo);
        assertEquals((Integer) 800, inventoryApi.getById(inventoryPojo.getProductId()).getQty());
    }

    @Test
    public void testUpdate() throws ApiException {
        InventoryPojo inventoryPojo = createInventoryPojo(0);
        inventoryApi.create(inventoryPojo);
        inventoryPojo.setQty(600);
        inventoryApi.update(inventoryPojo.getProductId(), inventoryPojo);
        assertEquals((Integer) 600, inventoryApi.getById(inventoryPojo.getProductId()).getQty());
    }

    @Test
    public void testGetInvalidId() throws ApiException {
        exceptionRule.expect(ApiException.class);
        inventoryApi.getById(-1);
    }

    private void matchPojo(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        ProductPojo productPojo = productApi.getById(inventoryPojo.getProductId());
        assertEquals("barcode" + id, productPojo.getBarcode());
        assertEquals((Integer) 400, inventoryPojo.getQty());
    }

    private InventoryPojo createInventoryPojo(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        Integer brandId = brandApi.create(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode" + id);
        productPojo.setBrandCategory(brandId);
        productPojo.setName("product" + id);
        productPojo.setMrp(100.0);
        Integer productId = productApi.create(productPojo);

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQty(400);

        return inventoryPojo;
    }

}
