package com.example.excercise2.controller;

import com.example.excercise2.entity.User;
import com.example.excercise2.model.LoginRequest;
import com.example.excercise2.model.LoginResponse;
import com.example.excercise2.model.UserInfo;
import com.example.excercise2.service.TokenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class LoginController {
    final TokenService tokenService;

    public LoginController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginRequest req) {
        return tokenService.login(req);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody LoginRequest req) {
        return tokenService.register(req);
    }

}