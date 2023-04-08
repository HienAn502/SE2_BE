package com.ecommerce.ecommerceweb.controller;

import com.ecommerce.ecommerceweb.datatransferobject.ResponseDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SigninDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SigninRespDTO;
import com.ecommerce.ecommerceweb.datatransferobject.user.SignupDTO;
import com.ecommerce.ecommerceweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    // signup
    @PostMapping("/signup")
    public ResponseDTO signup(@RequestBody SignupDTO signupDTO){
        return userService.signup(signupDTO);
    }

    //login
    @PostMapping("/signin")
    public SigninRespDTO signIn(@RequestBody SigninDTO signinDTO){
        return userService.signin(signinDTO);
    }
}
