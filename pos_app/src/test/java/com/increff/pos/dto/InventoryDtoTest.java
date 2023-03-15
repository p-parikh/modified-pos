package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryDto.create(inventoryForm);

        InventoryData inventoryData = inventoryDto.getAllData().get(0);
        matchData(0, inventoryData);
    }

    @Test
    public void testUnavailableBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("barcode1");

        exceptionRule .expect(ApiException.class);
        exceptionRule.expectMessage("Product with barcode: barcode1 does not exit!");
        inventoryDto.create(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testIncorrectBarcodeSizeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("barcode");
        inventoryDto.create(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNegativeQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQty((long)-1);
        inventoryDto.create(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("");
        inventoryDto.create(inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQty(null);
        inventoryDto.create(inventoryForm);
    }

    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
        for (Integer index = 0; index < 5; index++) {
            InventoryForm inventoryForm = createInventory(index);
            inventoryDto.create(inventoryForm);
        }
        List<InventoryData> inventoryDataList = inventoryDto.getAllData();
        assertEquals(5, inventoryDataList.size());
        for (Integer index = 0; index < 5; index++) {
            matchData(index, inventoryDataList.get(index));
        }
    }

    @Test
    public void testUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.create(inventoryForm);

        inventoryForm.setQty((long)115);
        inventoryDto.update(inventoryId, inventoryForm);

        InventoryData inventoryData = inventoryDto.getAllData().get(0);
        assertEquals((Integer) 115, inventoryData.getQty());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNegativeQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.create(inventoryForm);

        inventoryForm.setQty((long)-1);
        inventoryDto.update(inventoryId, inventoryForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.create(inventoryForm);
        inventoryForm.setQty(null);
        inventoryDto.update(inventoryId, inventoryForm);
    }


    private void matchData(Integer id, InventoryData inventoryData) {
        assertEquals("barcode" + id, inventoryData.getBarcode());
        assertEquals((Integer) 100, inventoryData.getQty());
    }

    private InventoryForm createInventory(Integer id) throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand" + id);
        brandForm.setCategory("category" + id);
        brandDto.create(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("barcode" + id);
        productForm.setBrand("brand" + id);
        productForm.setCategory("category" + id);
        productForm.setName("product" + id);
        productForm.setMrp(100.0);
        productDto.create(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("barcode" + id);
        inventoryForm.setQty((long)100);

        return inventoryForm;
    }

}