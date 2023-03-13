package com.increff.invoice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceData {

    private Integer invoiceNumber;
    private String invoiceDate;
    private String invoiceTime;
    private Double totalAmount;
    private List<InvoiceItemData> invoiceItemDataList;
}
