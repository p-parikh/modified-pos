package com.increff.invoice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemData {
    private Integer sno;
    private String barcode;
    private String productName;
    private Long quantity;
    private Double sellingPrice;
    private Double total;

}
