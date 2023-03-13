package com.increff.invoice.dto;

import com.increff.invoice.api.InvoiceApi;
import com.increff.invoice.model.InvoiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDto {
    @Autowired
    private InvoiceApi invoiceApi;

    public String generateInvoicePdf(InvoiceData invoiceData){
        return invoiceApi.generateInvoicePdf(invoiceData);
    }
}
