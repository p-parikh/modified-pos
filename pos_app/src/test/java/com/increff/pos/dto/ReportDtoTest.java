package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.model.forms.OrderItemForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    private ReportDto reportDto;

    @Autowired
    private OrderItemDto orderItemDto;

    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String START_TIME = "t00:00:00.000Z";
    public static final String END_TIME = "t23:59:59.000Z";
    @Test
    public void testBrandReport() throws Exception {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        createOrder(0);
        reportDto.getBrandReportData(httpServletResponse);
        assertEquals(HttpServletResponse.SC_OK,httpServletResponse.getStatus());
    }

    @Test
    public void testInventoryReport() throws Exception{
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        createOrder(0);
        reportDto.getInventoryReportData(httpServletResponse);
        assertEquals(HttpServletResponse.SC_OK,httpServletResponse.getStatus());
    }

    @Test
    public void testSalesReport() throws Exception{
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        List<OrderItemForm> orderItemFormList = createOrder(0);
        Integer orderId = orderItemDto.create(orderItemFormList);
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YMD);
        String currentDayDate = currentDateTime.format(dateTimeFormatter);
        reportDto.getSalesReportData(currentDayDate, currentDayDate,httpServletResponse);
        assertEquals(HttpServletResponse.SC_OK,httpServletResponse.getStatus());
    }
}
