package com.increff.pos.dto;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Autowired
    private WebApplicationContext webApplicationContext;

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


    @Test
    public void testUpload()
            throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "brand.tsv",
                "text/tab-separated-values",
                ("brand\tcategory\r\n" +
                        "brand0\tcategory0\u001a").getBytes()
        );
        brandDto.upload(file);
        List<BrandData> brandDataList = brandDto.getAllData();
        assertEquals(1,brandDataList.size());
    }
}
