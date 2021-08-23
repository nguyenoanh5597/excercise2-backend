package com.example.excercise2.controller;

import com.example.excercise2.model.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @GetMapping("/users")
    public List<UserInfo> getUsers() {
        UserInfo u1 = new UserInfo();
        u1.setUsername("oanh123");
        UserInfo u2 = new UserInfo();
        u2.setUsername("phu456");
        return Arrays.asList(u1, u2);
    }
}
