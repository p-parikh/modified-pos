package com.increff.invoice.controller;

import com.increff.invoice.dto.InvoiceDto;
import com.increff.invoice.model.InvoiceData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api
@Controller
@RequestMapping(path = "/api/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceDto invoiceDto;

    @ApiOperation(value = "Generate Invoice")
    @RequestMapping(path = "/generate-invoice", method = RequestMethod.POST)
    public String generateInvoice(@RequestBody InvoiceData invoiceData){
        return invoiceDto.generateInvoicePdf(invoiceData);
    }
}
