package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.InvoiceItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class PdfGeneratorDto {
    private static final String PDF_FILE_PATH = "src/main/resources/PdfFiles/invoice_";
    private final Logger logger = Logger.getLogger(PdfGeneratorDto.class);

    public static final String DATE_FORMAT_DMY = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";

//    @Value("${pdfApp.url}")
    private String pdfAppUrl = "http://localhost:9001/pdf/api/invoice/generate-invoice";

//    @Value("${pdfFilePath}")
    private String pdfFilePath = "src/main/resources/PdfFiles";
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductApi productApi;

    @Autowired
    private OrderItemApi orderItemApi;

    public void  generatePdf(Integer id) throws ApiException, JsonProcessingException {
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        OrderPojo orderPojo = orderApi.getById(id);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DMY);
        String currentDate = currentZonedDateTime.format(dateTimeFormatter);
        dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        String currentTime = currentZonedDateTime.format(dateTimeFormatter);
        InvoiceData invoiceData = getInvoiceDetails(orderPojo, currentDate, currentTime);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(invoiceData);
        System.out.println(json);
        String base64;
        ResponseEntity<String> response;
        try{
            response = restTemplate.postForEntity(pdfAppUrl, invoiceData, String.class);
            System.out.println(response.getBody());
        } catch (Exception e){
            e.printStackTrace();
            throw new ApiException("Unable to create invoice as invoice-app is not running.");
        }

        base64 = response.getBody();
        File pdfDir = new File(pdfFilePath);
        if(!pdfDir.mkdirs()){
            logger.info("PdfFiles folder created successfully");
        }
        String pdfFileName = "invoice_" + invoiceData.getInvoiceNumber() + ".pdf";
        File file = new File(pdfDir, pdfFileName);

        try (FileOutputStream fos = new FileOutputStream(file) ) {
            byte[] decoder = Base64.getDecoder().decode(base64);
            fos.write(decoder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private InvoiceData getInvoiceDetails(OrderPojo orderPojo, String currentDate, String currentTime) throws ApiException{
        List<OrderItemPojo> orderItemPojoList = orderItemApi.selectByOrderId(orderPojo.getId());
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceDate(currentDate);
        invoiceData.setInvoiceTime(currentTime);
        invoiceData.setInvoiceNumber(orderPojo.getId());
        Double totalAmount = 0.0;
        int sno = 1;
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            ProductPojo productPojo = productApi.getById(orderItemPojo.getProductId());
            invoiceItemData.setBarcode(productPojo.getBarcode());
            invoiceItemData.setSno(sno++);
            invoiceItemData.setProductName(productPojo.getName());
            invoiceItemData.setQuantity(orderItemPojo.getQuantity());
            invoiceItemData.setSellingPrice(orderItemPojo.getSellingPrice());
            invoiceItemData.setTotal(orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
            totalAmount += invoiceItemData.getTotal();
            invoiceItemDataList.add(invoiceItemData);
        }
        invoiceData.setTotalAmount(totalAmount);
        invoiceData.setInvoiceItemDataList(invoiceItemDataList);
        return invoiceData;
    }

    public String downloadInvoice(Integer orderId) throws IOException {
        String filePath = PDF_FILE_PATH + orderId + ".pdf";

        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();
        return Base64.getEncoder().encodeToString(bytesArray);
    }

}
