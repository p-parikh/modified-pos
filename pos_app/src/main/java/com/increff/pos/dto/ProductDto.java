package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.dto.helper.ProductDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FileConversionUtil;
import com.increff.pos.util.ValidationUtil;
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

    @Autowired
    private InventoryApi inventoryApi;

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
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        productPojo.setId(id);
        if(validateInput(productPojo)){
            productApi.update(id, ProductDtoHelper.normalise(productPojo));
        }
    }

    public Integer create(ProductForm productForm) throws ApiException{
        ValidationUtil.checkValid(productForm);
        BrandPojo brandPojo = brandApi.selectWithBrandAndCategory(productForm.getBrand(),
                productForm.getCategory());
        if(brandPojo == null){
            throw new ApiException("Product with provided brand and category does not exists");
        }
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm, brandPojo.getId());
        if(validateInput(productPojo)){
            productApi.create(ProductDtoHelper.normalise(productPojo));
        }
        return productPojo.getId();
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
            productForm.setBarcode((String) line.get(barcode));
            productForm.setMrp( Double.parseDouble((String) line.get(mrp)));
            productForm.setBrand((String) line.get(brand));
            productForm.setCategory((String) line.get(category));
            productForm.setName((String) line.get(name));
            System.out.println("Barcode: " + productForm.getBarcode() + " Mrp: " +
                    productForm.getMrp() + " Brand: " + productForm.getBrand() + " Category: " + productForm.getCategory() + " Name: " + productForm.getName());
            create(productForm);
        }
    }

    private boolean validateInput(ProductPojo productPojo) throws ApiException {
        List<Integer> listOfBrandIds = brandApi.selectAllIDs();
        if(!listOfBrandIds.contains(productPojo.getBrandCategory())){
            throw new ApiException("Please enter valid Brand Category");
        }
        ProductPojo productWithBarcode = productApi.selectWithBarcode(productPojo.getBarcode());

        if(productWithBarcode != null){
            if(productPojo.getId() != productWithBarcode.getId())
                throw new ApiException("Provided Product with given barcode already exists");
        }
        return true;
    }
}
