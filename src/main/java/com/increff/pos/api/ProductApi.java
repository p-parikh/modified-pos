package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    public List<ProductPojo> getAllEntries() {
        return productDao.viewAll();
    }

    public ProductPojo getById(Integer id) throws ApiException {
        ProductPojo productPojo_db = productDao.viewById(id);

        if (productPojo_db == null) {
            throw new ApiException("Product does not exists with given id");
        }

        return productPojo_db;
    }

    public void create(ProductPojo productPojo){ productDao.insert(productPojo);}

    public void update(Integer id, ProductPojo productPojo) throws ApiException{
        productDao.update(id, productPojo);
    }

    public ProductPojo selectWithBarcode(String barcode){

        return productDao.selectWithBarCode(barcode);
    }
}
