package com.example.excercise2.service;

import com.example.excercise2.entity.User;
import com.example.excercise2.model.LoginRequest;
import com.example.excercise2.model.LoginResponse;
import com.example.excercise2.model.UserInfo;
import com.example.excercise2.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TokenServiceImpl implements TokenService {
    static final String SECRET = "mySecretKeyDontLetYouKnowHaHaHa123456789aaaaaaa";
    static Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    final UserRepository userRepository;

    public TokenServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserInfo parseToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        UserInfo info = new UserInfo();
        info.setUsername(claims.getBody().get("username", String.class));
        info.setRoles(claims.getBody().get("roles", List.class));
        info.setUserId(claims.getBody().get("userId", String.class));
        info.setDisplayName(claims.getBody().get("displayName", String.class));
        return info;
    }

    @Override
    public LoginResponse login(LoginRequest user) {
        User existedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(Objects.nonNull(existedUser)){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(getJWTToken(existedUser));
            loginResponse.setUserInfo(parseToken(loginResponse.getToken()));
            return loginResponse;
        }
        throw new RuntimeException("Login Failed!");
    }

    @Override
    public User register(LoginRequest user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setDisplayName(user.getDisplayName());
        userRepository.save(newUser);
        return newUser;
    }

    private String getJWTToken(User user) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .createAuthorityList("user", "editor");
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getUserId());
        claims.put("displayName", user.getDisplayName());

        String token = Jwts
                .builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .claim("roles",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(key)
                .compact();
        return token;

    }




}
