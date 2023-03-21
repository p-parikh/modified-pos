package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
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

    public Integer create(ProductPojo productPojo){
        productDao.insert(productPojo);
        return productPojo.getId();
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException{
        productDao.update(id, productPojo);
    }

    public ProductPojo selectWithBarcode(String barcode){
        return productDao.selectWithBarCode(barcode);
    }

    public void createMultiple(List<ProductPojo> productPojoList) throws ApiException {
        int count = 1;
        for(ProductPojo productPojo : productPojoList){
            count++;
            ProductPojo productPojo_db = selectWithBarcode(productPojo.getBarcode());
            if(productPojo_db == null)
                productDao.insert(productPojo);
            else
                throw new ApiException("Provided barcode already exists. Error on line number: " +count);
        }
    }
}
