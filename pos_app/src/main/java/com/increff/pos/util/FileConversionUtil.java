package com.increff.pos.util;

import com.increff.pos.exception.ApiException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileConversionUtil {
    public static File convert(MultipartFile file) throws IOException, ApiException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        String fileExtension = FilenameUtils.getExtension(convFile.toString());
        if(!fileExtension.equals("tsv")){
            throw new ApiException("Input file is not a valid TSV file");
        }
        return convFile;
    }
}
