package com.increff.pos.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.OrderItemForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

public class PdfGeneratorDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private PdfGeneratorDto pdfGeneratorDto;

    @Test(expected = ApiException.class)
    public void testGeneratePdfForIncorrectOrderId() throws ApiException, JsonProcessingException {
        pdfGeneratorDto.generatePdf(2);
    }

    @Test
    public void testDownloadInvoice() throws IOException, ApiException, IllegalAccessException {
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);
        pdfGeneratorDto.generatePdf(orderId);
        assertNotEquals(null, pdfGeneratorDto.downloadInvoice(orderId));
    }


}
