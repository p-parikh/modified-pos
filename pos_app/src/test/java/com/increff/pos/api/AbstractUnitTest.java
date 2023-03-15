package com.increff.pos.api;

import com.increff.pos.config.QaConfig;
import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.forms.BrandForm;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.model.forms.ProductForm;
import com.increff.pos.pojo.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private OrderApi orderApi;

    protected void createAndPlaceOrder() throws ApiException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            List<OrderItemForm> orderItemFormList = createOrder(i);
            Integer orderId = orderItemDto.create(orderItemFormList);
        }
    }

    protected List<OrderItemForm> createOrder(Integer id) throws ApiException, IllegalAccessException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand" + id);
        brandForm.setCategory("category" + id);
        brandDto.create(brandForm);

        //product1
        ProductForm productForm1 = new ProductForm();
        productForm1.setBarcode("barcode" + id);
        productForm1.setBrand("brand" + id);
        productForm1.setCategory("category" + id);
        productForm1.setName("product" + id);
        productForm1.setMrp(100.0);
        productDto.create(productForm1);

        //product2
        ProductForm productForm2 = new ProductForm();
        productForm2.setBarcode(id + "barcode");
        productForm2.setBrand("brand" + id);
        productForm2.setCategory("category" + id);
        productForm2.setName(id + "product");
        productForm2.setMrp(200.0);
        productDto.create(productForm2);


        //product1 add inventory
        InventoryForm inventoryForm1 = new InventoryForm();
        inventoryForm1.setBarcode("barcode" + id);
        inventoryForm1.setQty((long) 400);
        inventoryDto.create(inventoryForm1);

        //product2 add inventory
        InventoryForm inventoryForm2 = new InventoryForm();
        inventoryForm2.setBarcode(id + "barcode");
        inventoryForm2.setQty((long) 400);
        inventoryDto.create(inventoryForm2);


        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm1 = new OrderItemForm();
        orderItemForm1.setBarcode("barcode" + id);
        orderItemForm1.setQuantity(200);
        orderItemForm1.setSellingPrice(98.5);
        orderItemFormList.add(orderItemForm1);

        OrderItemForm orderItemForm2 = new OrderItemForm();
        orderItemForm2.setBarcode(id + "barcode");
        orderItemForm2.setQuantity(200);
        orderItemForm2.setSellingPrice(198.5);
        orderItemFormList.add(orderItemForm2);
        return orderItemFormList;
    }

    public List<OrderItemPojo> createOrderItemPojoList(Integer id) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + id);
        brandPojo.setCategory("category" + id);
        Integer brandId = brandApi.create(brandPojo);

        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setBarcode("barcode" + id);
        productPojo1.setBrandCategory(brandId);
        productPojo1.setName("product" + id);
        productPojo1.setMrp(100.0);
        Integer product1Id = productApi.create(productPojo1);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setBarcode(id + "barcode");
        productPojo2.setBrandCategory(brandId);
        productPojo2.setName(id + "product");
        productPojo2.setMrp(100.0);
        Integer product2Id = productApi.create(productPojo2);


        InventoryPojo inventoryPojo1 = new InventoryPojo();
        inventoryPojo1.setProductId(product1Id);
        inventoryPojo1.setQty((long) 400);
        inventoryApi.create(inventoryPojo1);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setProductId(product2Id);
        inventoryPojo2.setQty((long) 400);
        inventoryApi.create(inventoryPojo2);

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();

        OrderItemPojo orderItemPojo1 = new OrderItemPojo();
        orderItemPojo1.setProductId(product1Id);
        orderItemPojo1.setQuantity(10);
        orderItemPojo1.setSellingPrice(20.0);
        orderItemPojoList.add(orderItemPojo1);

        OrderItemPojo orderItemPojo2 = new OrderItemPojo();
        orderItemPojo2.setProductId(product2Id);
        orderItemPojo2.setQuantity(10);
        orderItemPojo2.setSellingPrice(20.0);
        orderItemPojoList.add(orderItemPojo2);

        return orderItemPojoList;
    }
}
