package com.example.excercise2.controller;

import com.example.excercise2.model.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

public class BaseController {
    protected UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserInfo) authentication.getDetails();
    }
}
