package com.increff.pos.model.invoice;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceItemData {

    @XmlElement(name = "barcode")
    private String barcode;

    @XmlElement(name = "product-name")
    private String productName;

    @XmlElement(name = "quantity")
    private Long quantity;

    @XmlElement(name = "selling-price")
    private Double sellingPrice;

    @XmlElement(name = "amount")
    private Double amount;

}
