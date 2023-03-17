package com.increff.pos.dto;

import com.increff.commons.sheet.TsvToJson;
import com.increff.pos.api.BrandApi;
import com.increff.pos.dto.helper.BrandDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.FileConversionUtil;
import com.increff.pos.util.TsvUtil;
import com.increff.pos.util.ValidationUtil;
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

    @Autowired
    private TsvUtil tsvUtil;

    public List<BrandData> getAllData(){
        List<BrandData> resultSet = new ArrayList<>();
        for (BrandPojo bp : brandApi.getAllEntries()) {
            resultSet.add(BrandDtoHelper.convertToBrandData(bp));
        }
        return resultSet;
    }

    public void update(Integer id, BrandForm brandForm) throws ApiException {
        ValidationUtil.checkValid(brandForm);
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        brandPojo.setId(id);
        brandApi.update(id, BrandDtoHelper.normalise(brandPojo));
    }

    public Integer create(BrandForm brandForm) throws ApiException {
        ValidationUtil.checkValid(brandForm);
        BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
        brandApi.create(BrandDtoHelper.normalise(brandPojo));
        return brandPojo.getId();
    }

    public void upload(MultipartFile brandTsv) throws Exception {
        File convertedTsv = FileConversionUtil.convert(brandTsv); // multipart file to file conversion
        List<BrandForm> uploadList = tsvUtil.convert(convertedTsv,BrandForm.class); // converting file into list of class objects
        ValidationUtil.checkValid(uploadList);
        for(BrandForm brandForm : uploadList){
            BrandPojo brandPojo = BrandDtoHelper.convertToBrandPojo(brandForm);
            brandApi.create(BrandDtoHelper.normalise(brandPojo));
        }
    }
}
