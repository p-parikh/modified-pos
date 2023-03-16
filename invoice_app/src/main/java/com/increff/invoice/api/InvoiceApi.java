package com.increff.invoice.api;

import com.increff.invoice.model.InvoiceData;
import com.increff.invoice.util.JavaToXml;
import com.increff.invoice.util.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceApi {
    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private JavaToXml javaToXml;

    public String generateInvoicePdf(InvoiceData invoiceData){
        javaToXml.javaToXmlConverter(invoiceData);
        String result = pdfGenerator.xmlToPdfConverter();
        System.out.println("in api class, and result : " +result);
        return result;
    }
}
