package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;

import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.ProductForm;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

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

    @Test(expected = ApiException.class)
    public void testUnavailableBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("barcode1");
        try{
            inventoryDto.create(inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Product with provided barcode does not exists", e.getMessage());
            throw new ApiException("Product with provided barcode does not exists");
        }
    }

    @Test(expected = ApiException.class)
    public void testNegativeQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQty(-1);
        try{
            inventoryDto.create(inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }

    }

    @Test(expected = ApiException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setBarcode("");
        try{
            inventoryDto.create(inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyQuantityOnAdd() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        inventoryForm.setQty(null);
        try{
            inventoryDto.create(inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
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

        inventoryForm.setQty(115);
        inventoryDto.update(inventoryId, inventoryForm);

        InventoryData inventoryData = inventoryDto.getAllData().get(0);
        assertEquals((Integer) 115, inventoryData.getQty());
    }

    @Test(expected = ApiException.class)
    public void testNegativeQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.create(inventoryForm);
        inventoryForm.setQty(-1);
        try{
            inventoryDto.update(inventoryId, inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyQuantityOnUpdate() throws ApiException, IllegalAccessException {
        InventoryForm inventoryForm = createInventory(0);
        Integer inventoryId = inventoryDto.create(inventoryForm);
        inventoryForm.setQty(null);
        try{
            inventoryDto.update(inventoryId, inventoryForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
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
        inventoryForm.setQty(100);

        return inventoryForm;
    }

    @Test
    public void testUpload()
            throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "inventory.tsv",
                "text/tab-separated-values",
                ("barcode\tqty\r\n" +
                        "barcode0\t100\u001a").getBytes()
        );
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand0");
        brandForm.setCategory("category0");
        brandDto.create(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("barcode0");
        productForm.setBrand("brand0");
        productForm.setCategory("category0");
        productForm.setName("product0");
        productForm.setMrp(100.0);
        productDto.create(productForm);

        inventoryDto.upload(file);
        List<InventoryData> inventoryDataList = inventoryDto.getAllData();
        assertEquals(1,inventoryDataList.size());
    }

}