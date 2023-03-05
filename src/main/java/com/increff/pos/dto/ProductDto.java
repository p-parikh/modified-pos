package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FileConversionUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BrandApi brandApi;
    public List<ProductData> getAllData() throws ApiException{
        List<ProductData> resultSet = new ArrayList<>();
        for(ProductPojo pp : productApi.getAllEntries()){
            BrandPojo brandPojo = brandApi.getById(pp.getBrandCategory());
            resultSet.add(ProductDtoHelper.convertToProductData(pp, brandPojo));
        }
        return resultSet;
    }

    public ProductData getById(Integer id) throws ApiException {
        ProductPojo productPojo = productApi.getById(id);
        BrandPojo brandPojo = brandApi.getById(productPojo.getBrandCategory());
        return ProductDtoHelper.convertToProductData(productPojo, brandPojo);
    }

    public void update(Integer id, ProductForm productForm) throws ApiException{
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        if(ProductDtoHelper.validateInput(productPojo)){
            productApi.update(id, ProductDtoHelper.normalise(productPojo));
        }
    }

    public void create(ProductForm productForm) throws ApiException{
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        if(ProductDtoHelper.validateInput(productPojo)){
            productApi.create(ProductDtoHelper.normalise(productPojo));
        }
    }

    public void upload(MultipartFile productTsv) throws Exception {
        File convertedTsv = FileConversionUtil.convert(productTsv);
        String fileExtension = FilenameUtils.getExtension(convertedTsv.toString());
        if(!fileExtension.equals("tsv")){
            throw new ApiException("Input file is not a valid TSV file");
        }
        TsvToJson tsvParse =     new TsvToJson();
        List<HashMap<String, Object>> values = tsvParse.tsvToJson(convertedTsv);
        String name = "name";
        String mrp = "mrp";
        String barcode = "barcode";
        String brand = "brand";
        String category = "category";
        for(HashMap<String, Object> line : values){
            ProductForm productForm = new ProductForm();
            productForm.setBarcode((String)line.get(barcode));
            productForm.setMrp((Double) line.get(mrp));
            productForm.setBrand((String) line.get(brand));
            productForm.setCategory((String) line.get(category));
            productForm.setName((String) line.get(name));
            create(productForm);
        }
    }

}
