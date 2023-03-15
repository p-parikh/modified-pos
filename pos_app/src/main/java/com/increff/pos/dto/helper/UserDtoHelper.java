package com.increff.pos.dto.helper;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Component;

public class UserDtoHelper {

    public static UserData convertToUserData(UserPojo userPojo){
        UserData userData = new UserData();
        userData.setEmail(userPojo.getEmail());
        userData.setRole(userPojo.getRole());
        userData.setId(userPojo.getId());
        userData.setPassword(userPojo.getPassword());
        return userData;
    }

    public static UserPojo convertToUserPojo(UserForm userForm){
        UserPojo userPojo = new UserPojo();
        userPojo.setPassword(userForm.getPassword());
        userPojo.setEmail(userForm.getEmail());
        return userPojo;
    }

    public static UserPojo normalise(UserPojo userPojo){
        userPojo.setEmail(userPojo.getEmail().toLowerCase().trim());
        return userPojo;
    }
}
