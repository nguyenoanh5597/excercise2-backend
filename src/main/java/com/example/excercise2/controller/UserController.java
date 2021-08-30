package com.example.excercise2.controller;

import com.example.excercise2.entity.User;
import com.example.excercise2.model.UserInfo;
import com.example.excercise2.repositories.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController extends BaseController {
    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<UserInfo> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserInfo> allUsers = new ArrayList();
        for (User user : users) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getUserId());
            userInfo.setUsername(user.getUsername());
            userInfo.setDisplayName(user.getDisplayName());
            userInfo.setRoles(Arrays.asList("user", "editor"));
            allUsers.add(userInfo);
        }
        return allUsers;
    }

    @GetMapping("/{userId}")
    public UserInfo getUserById(@PathVariable("userId") String userId) {
        User user = userRepository.findByUserId(userId);
        if (Objects.isNull(user)) {
            throw new RuntimeException("User not found");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setRoles(Arrays.asList("user", "editor"));
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setDisplayName(user.getDisplayName());
        return userInfo;
    }
}
