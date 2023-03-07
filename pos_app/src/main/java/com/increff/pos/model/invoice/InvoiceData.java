package com.increff.pos.model.invoice;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "invoice-record")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceData {

    @XmlElement(name = "order-id")
    private Integer orderId;

    @XmlElement(name = "invoice-number")
    private Long invoiceNumber;

    @XmlElement(name = "invoice-date")
    private String invoiceDate;

    @XmlElement(name = "invoice-total")
    private Double invoiceTotal;

    @XmlElement(name = "line-item-record")
    @XmlElementWrapper(name="line-item-records")
    private List<InvoiceItemData> invoiceItemDataList;
}
