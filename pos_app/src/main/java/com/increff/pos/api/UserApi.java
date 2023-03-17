package com.increff.pos.api;

import com.increff.pos.dao.UserDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserPrincipal;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserApi {

    @Autowired
    private UserDao userDao;

    @Value("$supervisor.email")
    private String supervisorEmail;

    public void addUser(UserPojo userPojo) throws ApiException{
        UserPojo userPojo_db = userDao.selectByEmail(userPojo.getEmail());

        if(userPojo_db != null){
            throw new ApiException("User with provided email id already exists.");
        }

        userDao.insert(userPojo);
    }

    public List<UserPojo> getAllEntries(){
        return userDao.viewAll();
    }

    public UserPojo getByEmail(String email) throws ApiException{
        UserPojo userPojo_db = userDao.selectByEmail(email);
        if(userPojo_db == null){
            throw new ApiException("User with provided email id does not exists");
        }
        return userPojo_db;
    }

    public void update(Integer id, UserPojo userPojo) throws ApiException{
        UserPojo userPojo_db = userDao.viewById(id);
        UserPrincipal principal = SecurityUtil.getPrincipal();
        if(principal == null){
            throw new ApiException("Unable to update user");
        }
        if(userPojo.getEmail().equals(supervisorEmail)){
            if(principal.getEmail().equals(supervisorEmail)){
                userPojo_db.setPassword(userPojo.getPassword());
            }
            else
                throw new ApiException("Cannot edit this supervisor");
        }
        else{
            userPojo_db.setPassword(userPojo.getPassword());
            userPojo_db.setRole(userPojo.getRole());
        }
    }
}
