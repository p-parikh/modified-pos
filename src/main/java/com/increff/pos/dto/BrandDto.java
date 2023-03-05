package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.BrandApi;
import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.FileConversionUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class BrandDto {
    @Autowired
    private BrandApi brandApi;

    public List<BrandData> getAllData(){
        List<BrandData> resultSet = new ArrayList<>();
        for (BrandPojo bp : brandApi.getAllEntries()) {
            resultSet.add(BrandDtoHelper.convertToBrandData(bp));
        }
        return resultSet;
    }

    public BrandData getById(Integer id) throws ApiException{
        return BrandDtoHelper.convertToBrandData(brandApi.getById(id));
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        if (BrandDtoHelper.validateInput(brandPojo)) {
            brandApi.update(id, BrandDtoHelper.normalise(brandPojo));
        }
    }

    public void create(BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        if (BrandDtoHelper.validateInput(brandPojo)) {
            brandApi.create(BrandDtoHelper.normalise(brandPojo));
        }
    }

    public void upload(MultipartFile brandTsv) throws Exception {
        File convertedTsv = FileConversionUtil.convert(brandTsv);
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
}
