package com.example.excercise2.filter;

import com.example.excercise2.exception.InvalidJwtAuthenticationException;
import com.example.excercise2.security.JwtAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter(String matcher, AuthenticationManager authenticationManager) {
        super(matcher, authenticationManager);
    }

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private final String SECRET = "mySecretKeyDontLetYouKnowHaHaHa@123456789";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization)) {
            // TODO: check if user added Authorization header but value is not in format "Bearer <token>"
            String token = authorization.substring("Bearer ".length());
            Authentication jwtAuth = new JwtAuthentication(token);
            return getAuthenticationManager().authenticate(jwtAuth);
        } else {
            throw new InvalidJwtAuthenticationException("Missing authorization header");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult.isAuthenticated()) {
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authResult);
            chain.doFilter(request, response);
        } else {
            throw new InvalidJwtAuthenticationException("Fail to authenticate request");
        }
    }

//    private Claims validateToken(HttpServletRequest request) {
//        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
//        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
//    }
//
//    /**
//     * Authentication method in Spring flow
//     *
//     * @param claims
//     */
//    private void setUpSpringAuthentication(io.jsonwebtoken.Claims claims) {
//        @SuppressWarnings("unchecked")
//        List<String> authorities = (List) claims.get("authorities");
//
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//    }
//
//    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
//        String authenticationHeader = request.getHeader(HEADER);
//        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
//            return false;
//        return true;
//    }
}
