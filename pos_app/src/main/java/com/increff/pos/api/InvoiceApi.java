package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.invoice.InvoiceData;
import com.increff.pos.model.invoice.InvoiceItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class InvoiceApi {
    private static StringBuilder fileName;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderDao orderDao;

    public void generateInvoice(Integer orderId) throws Exception {
        OrderPojo orderPojo = orderDao.viewById(orderId);
        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectWithOrderId(orderId);

        try{
            File xslFile = new File(Thread.currentThread().getContextClassLoader().getResource("Template.xsl").toURI());
            String xmlInput = getXmlString(orderItemPojoList, orderPojo);
            createInvoicePdf(xmlInput, xslFile);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ApiException("Issue generating xml for orders");
        }
    }

    private String getXmlString(List<OrderItemPojo> orderItemPojoList, OrderPojo orderPojo) throws Exception{
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceItemDataList(convertOrderItemPojoToInvoiceItemData(orderItemPojoList));
        Long invoiceNumber = ThreadLocalRandom.current().nextLong(100000,1000000000);
        invoiceData.setInvoiceNumber(invoiceNumber);
        invoiceData.setInvoiceDate(new Timestamp(System.currentTimeMillis()).toString());
        invoiceData.setOrderId(orderPojo.getId());
        invoiceData.setInvoiceTotal(orderItemPojoList.stream().mapToDouble(orderItemPojo -> orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice()).sum());
        StringWriter stringWriter = new StringWriter();
        try{
            JAXBContext context = JAXBContext.newInstance(InvoiceData.class);
            Marshaller marshallerObj = context.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(invoiceData,stringWriter);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new ApiException("Issue while generating XML for orders");
        }
        return stringWriter.toString();
    }

    public void createInvoicePdf(String xmlInputString, File xslFile){

    }

    private List<InvoiceItemData> convertOrderItemPojoToInvoiceItemData(List<OrderItemPojo> orderItemPojoList){
        List<InvoiceItemData> resultSet = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            InvoiceItemData data = new InvoiceItemData();
            ProductPojo productPojo = productDao.viewById(orderItemPojo.getProductId());
            data.setBarcode(productPojo.getBarcode());
            data.setProductName(productPojo.getName());
            data.setQuantity(orderItemPojo.getQuantity());
            data.setSellingPrice(orderItemPojo.getSellingPrice());
            data.setAmount(orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity());
            resultSet.add(data);
        }
        return resultSet;
    }

}
