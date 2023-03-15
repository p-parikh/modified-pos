package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertNotEquals;

public class PdfGeneratorDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private PdfGeneratorDto pdfGeneratorDto;

    @Test(expected = ApiException.class)
    public void testGeneratePdfForIncorrectOrderId() throws ApiException, IllegalAccessException, IOException {
        pdfGeneratorDto.generatePdf(2);
    }

    @Test
    public void testDownloadInvoice() throws IOException {
        assertNotEquals(null, pdfGeneratorDto.downloadInvoice(1));
    }


}
