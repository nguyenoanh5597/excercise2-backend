package com.example.excercise2.service;

import com.example.excercise2.entity.User;
import com.example.excercise2.model.LoginRequest;
import com.example.excercise2.model.LoginResponse;
import com.example.excercise2.model.UserInfo;

public interface TokenService {
    UserInfo parseToken(String token);
    LoginResponse login(LoginRequest user);
    User register(LoginRequest user);
}
