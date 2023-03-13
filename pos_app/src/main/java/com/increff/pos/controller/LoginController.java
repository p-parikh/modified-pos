package com.increff.pos.controller;

import com.increff.pos.dto.UserDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api
@RestController
@RequestMapping(path = "/session")
public class LoginController{

    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "User Login")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest httpServletRequest, @RequestBody LoginForm loginForm) throws ApiException {
        ValidationUtil.validate(loginForm);
        loginForm.setEmail(loginForm.getEmail().toLowerCase().trim());
        UserData userData = userDto.getByEmail(loginForm.getEmail());
        boolean authenticated = (userData != null) && (userData.getPassword().equals(loginForm.getPassword()));
        if(!authenticated){
            throw new ApiException("Invalid username or password");
        }
        Authentication authentication = userDto.convertUserPojoToAuthentication(userData);
        HttpSession session = httpServletRequest.getSession(true);
        SecurityUtil.createContext(session);
        SecurityUtil.setAuthentication(authentication);
    }

    @ApiOperation(value = "Logout")
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().invalidate();
    }


    @ApiOperation(value = "sign up")
    @RequestMapping(path = "/sign-up", method = RequestMethod.POST)
    public void SignUp(@RequestBody UserForm userForm) throws ApiException, IllegalAccessException {
        userDto.addUser(userForm);
    }

}
