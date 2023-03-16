package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.ProductForm;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);

        ProductData productData = productDto.getAllData().get(0);
        matchData(0, productData);
    }

    @Test(expected = ApiException.class)
    public void testUniqueBarcodeOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);
        try{
            productDto.create(productForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Provided Product with given barcode already exists", e.getMessage());
            throw new ApiException("Provided Product with given barcode already exists");
        }
    }

    @Test(expected = ApiException.class)
    public void testUnavailableBrandCategoryPairOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);

        productForm.setBrand("brand");
        productForm.setCategory("category");

        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Product with provided brand and category does not exists", e.getMessage());
            throw new ApiException("Product with provided brand and category does not exists");
        }

    }

    @Test(expected = ApiException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productForm.setBarcode("");
        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyBrandOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productForm.setBrand("");
        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyCategoryOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productForm.setCategory("");
        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyProductNameOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productForm.setName("");
        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }    }

    @Test(expected = ApiException.class)
    public void testEmptyMrpOnAdd() throws ApiException {
        ProductForm productForm = createProduct(0);
        productForm.setMrp(null);
        try{
            productDto.create(productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test
    public void testGetAll() throws ApiException {
        for(Integer index = 0; index < 5; index++) {
            ProductForm productForm = createProduct(index);
            productDto.create(productForm);
        }
        List<ProductData> productDataList = productDto.getAllData();
        assertEquals(5, productDataList.size());
        for(Integer index = 0; index < 5; index++) {
            matchData(index, productDataList.get(index));
        }
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);
        productForm.setName("new product");
        productForm.setMrp(200.0);
        productDto.update(productId, productForm);
        ProductData productData = productDto.getAllData().get(0);
        assertEquals("new product", productData.getName());
        assertEquals((Double) 200.0, productData.getMrp());
    }

    @Test(expected = ApiException.class)
    public void testEmptyProductNameOnUpdate() throws ApiException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);
        productForm.setName("");
        productForm.setMrp(200.0);
        try{
            productDto.update(productId, productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testEmptyMrpOnUpdate() throws ApiException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);
        productForm.setName("new name");
        productForm.setMrp(null);
        try{
            productDto.update(productId, productForm);
        }
        catch(ApiException e){
            Assert.assertEquals("Input validation failed", e.getMessage());
            throw new ApiException("Input validation failed");
        }
    }

    @Test(expected = ApiException.class)
    public void testAddOnBarcodeAlreadyExist() throws ApiException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);
        try{
            productDto.create(productForm);
        }
        catch (ApiException e){
            Assert.assertEquals("Provided Product with given barcode already exists",e.getMessage());
            throw new ApiException("Provided Product with given barcode already exists");
        }
    }

    private void matchData(Integer id, ProductData productData){
        assertEquals("barcode" + id, productData.getBarcode());
        assertEquals("brand" + id, productData.getBrand());
        assertEquals("category" + id, productData.getCategory());
        assertEquals("product" + id, productData.getName());
    }

    private ProductForm createProduct(Integer id) throws ApiException {
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
        return productForm;
    }

}