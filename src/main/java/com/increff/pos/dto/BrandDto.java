package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class BrandDto {
    @Autowired
    private BrandService brandService;

    public List<BrandData> getAllData(){
        List<BrandData> resultSet = new ArrayList<>();
        for(BrandPojo bp:brandService.getAllEntries()){
           resultSet.add(BrandDtoHelper.convertToBrandData(bp));
        }
        return resultSet;
    }

    public BrandData getById(Integer id) throws ApiException{
      return  BrandDtoHelper.convertToBrandData(brandService.getById(id));
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException{
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        if(validateInput(brandPojo)){
            brandService.update(id, normalise(brandPojo));
        }
    }

    public void create(BrandForm brandForm) throws ApiException{
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        if(validateInput(brandPojo)) {
            brandService.create(normalise(brandPojo));
        }
    }

    public void upload(MultipartFile brandTsv) throws ApiException,IOException,Exception {
        File convertedTsv = convert(brandTsv);
        String fileExtension = FilenameUtils.getExtension(convertedTsv.toString());
        if(!fileExtension.equals("tsv")){
            throw new ApiException("Input file is not a valid TSV file");
        }
        TsvToJson tsvParse = new TsvToJson();
        List<HashMap<String, Object>> values = tsvParse.tsvToJson(convertedTsv);
        String brand = "brand";
        String category = "category";
        for(HashMap<String, Object> line : values){
            BrandForm brandForm = new BrandForm();
            brandForm.setCategory((String) line.get(brand));
            brandForm.setBrand((String) line.get(category));
            create(brandForm);
        }
    }

    public BrandPojo normalise(BrandPojo brandPojo){
        brandPojo.setBrand(brandPojo.getBrand().trim().toLowerCase());
        brandPojo.setCategory(brandPojo.getCategory().trim().toLowerCase());
        return brandPojo;
    }

    public boolean validateInput(BrandPojo brandPojo) throws ApiException {
        if(brandPojo.getBrand() == null)
            throw new ApiException("Please enter valid Brand");
        if(brandPojo.getCategory() == null)
            throw new ApiException("Please enter valid Category");
        List<BrandPojo> listOfDataWithBrandCategoryCombo = brandService
                .getAlByBrandCategoryCombination(brandPojo.getBrand(), brandPojo.getCategory());
        if(!listOfDataWithBrandCategoryCombo.isEmpty()){
          if(listOfDataWithBrandCategoryCombo.size() == 1){
             BrandPojo brandPojo_db =  listOfDataWithBrandCategoryCombo.get(0);
             if(brandPojo_db.getId() != brandPojo.getId())
                 throw new ApiException("Provided Brand Category Pair already exists");
          }
          else
              throw new ApiException("Provided Brand Category Pair already exists");
        }
        return true;
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
