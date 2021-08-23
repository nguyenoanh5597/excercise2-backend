package com.example.excercise2.controller;

import com.example.excercise2.model.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        UserDTO u1 = new UserDTO();
        u1.setName("oanh123");
        UserDTO u2 = new UserDTO();
        u2.setName("phu456");
        return Arrays.asList(u1, u2);
    }
}
