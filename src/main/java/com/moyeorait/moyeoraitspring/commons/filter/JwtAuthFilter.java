package com.moyeorait.moyeoraitspring.commons.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String JWT_AUTH_URL = "";

    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;

    public JwtAuthFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = new RestTemplate();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if(token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String userId = redisTemplate.opsForValue().get(token);

        if(userId == null) {
            userId = validateTokenWithNode(token);
            if(userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        request.setAttribute("userId", userId);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null || header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

    private String validateTokenWithNode(String token) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    JWT_AUTH_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if(response.getStatusCode() == HttpStatus.OK){
                return (String) response.getBody().get("userId");
            }

        } catch (Exception e){
            return null;
        }
        return null;
    }
}
