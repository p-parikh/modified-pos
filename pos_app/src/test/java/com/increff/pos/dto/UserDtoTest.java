package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private UserDto userDto;

    @Test
    public void testAddUser() throws ApiException, IllegalAccessException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@gmail.com");
        userForm.setPassword("12345678");
        userDto.addUser(userForm);
        UserData userData = userDto.getAllUsers().get(0);
        assertEquals("test@gmail.com", userData.getEmail());
        assertEquals("operator", userData.getRole());
        assertEquals("12345678", userData.getPassword());
    }


    @Test(expected = ApiException.class)
    public void testUpdateUser() throws ApiException, IllegalAccessException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@gmail.com");
        userForm.setPassword("12345678");
        userDto.addUser(userForm);
        UserData userData = userDto.getAllUsers().get(0);
        userForm.setPassword("88888888");
        userDto.updateUser(userData.getId(), userForm);
    }




}
