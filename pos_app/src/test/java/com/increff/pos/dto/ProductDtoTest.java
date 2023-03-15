package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.ProductForm;
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
    public void testAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);

        ProductData productData = productDto.getAllData().get(0);
        matchData(0, productData);
    }

    @Test
    public void testUniqueBarcodeOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Provided Product with given barcode already exists");
        productDto.create(productForm);
    }

    @Test
    public void testUnavailableBrandCategoryPairOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productDto.create(productForm);

        productForm.setBrand("brand");
        productForm.setCategory("category");

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please enter valid Brand Category");
        productDto.create(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBarcodeOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setBarcode("");
        productDto.create(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyBrandOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setBrand("");
        productDto.create(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyCategoryOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setCategory("");
        productDto.create(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyProductNameOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setName("");
        productDto.create(productForm);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyMrpOnAdd() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        productForm.setMrp(null);
        productDto.create(productForm);
    }

    @Test
    public void testGetAll() throws ApiException, IllegalAccessException {
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
    public void testUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);

        ProductForm productForm1 = new ProductForm();
        productForm1.setName("new product");
        productForm1.setMrp(200.0);
        productDto.update(productId, productForm1);

        ProductData productData = productDto.getAllData().get(0);
        assertEquals("new product", productData.getName());
        assertEquals((Double) 200.0, productData.getMrp());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyProductNameOnUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);
        ProductForm productForm1 = new ProductForm();
        productForm1.setName("");
        productForm1.setMrp(200.0);
        productDto.update(productId, productForm1);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmptyMrpOnUpdate() throws ApiException, IllegalAccessException {
        ProductForm productForm = createProduct(0);
        Integer productId = productDto.create(productForm);
        ProductForm productForm1 = new ProductForm();
        productForm1.setName("new name");
        productForm1.setMrp(null);
        productDto.update(productId, productForm1);
    }

    private void matchData(Integer id, ProductData productData){
        assertEquals("barcode" + id, productData.getBarcode());
        assertEquals("brand" + id, productData.getBrand());
        assertEquals("category" + id, productData.getCategory());
        assertEquals("product" + id, productData.getName());
    }

    private ProductForm createProduct(Integer id) throws ApiException, IllegalAccessException {
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