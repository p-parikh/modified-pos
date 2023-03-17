package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BrandApi {

    @Autowired
    private BrandDao brandRepo;

    public List<BrandPojo> getAllEntries() {
        return brandRepo.viewAll();
    }

    public BrandPojo getById(Integer id) throws ApiException{
        BrandPojo db_bp = brandRepo.getById(id);
        if(db_bp == null){
            throw new ApiException("Brand does not exists with given Id");
        }
        return db_bp;
    }

    public Integer create(BrandPojo bp){
        brandRepo.insert(bp);
        return bp.getId();
    }

    public void update(Integer id, BrandPojo new_bp) throws ApiException{
        brandRepo.update(id, new_bp);
    }

    public BrandPojo selectWithBrandAndCategory(String brand, String category){
        return brandRepo.selectWithBrandAndCategory(brand, category);
    }

}
