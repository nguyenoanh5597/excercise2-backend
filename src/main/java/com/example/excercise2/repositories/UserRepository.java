package com.example.excercise2.repositories;

import com.example.excercise2.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsernameAndPassword(String username, String password);
    User findByUserId(String userId);
}
