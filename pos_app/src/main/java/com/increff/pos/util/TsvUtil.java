package com.increff.pos.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.commons.sheet.TsvToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class TsvUtil {

    private static TsvToJson tsvParse;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> List<T> convert(File file, Class<T> clazz) throws Exception {
        tsvParse = new TsvToJson();
        List<HashMap<String, Object>> values = tsvParse.tsvToJson(file);
        List<T> resultSet = new ArrayList<>();
        for(Map<String, Object> value : values){
            resultSet.add(objectMapper.convertValue(value, clazz));
        }
        return resultSet;
    }
}
