package com.moyeorait.moyeoraitspring.commons.filter;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfo;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import com.moyeorait.moyeoraitspring.domain.user.UserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String JWT_AUTH_URL = "https://my-api.sjcpop.com/api/v1/user/info";
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
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
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("userId", null);
            filterChain.doFilter(request, response);
            return;
        }

//        String userId = redisTemplate.opsForValue().get(token);
//
//        if(userId == null) {
//            userId = validateTokenWithNode(token);
//            if(userId == null) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//        request.setAttribute("userId", userId);
//        filterChain.doFilter(request, response);

        String userId = findUserInfoByTokenAndNode(token);
        log.debug("Attribute setting : {}", userId);
        request.setAttribute("userId", userId);

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        log.debug("extractToken");

        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            log.debug("header : {}", header);
            return header.substring(7);
        }
        return null;
    }

    private String findUserInfoByTokenAndNode(String token) {
        log.debug("findUserOfNodeServer token:" , token);
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "accessToken=" + token);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<NodeUserInfoResponse> response = restTemplate.exchange(
                    JWT_AUTH_URL,
                    HttpMethod.GET,
                    entity,
                    NodeUserInfoResponse.class
            );
            NodeUserInfoResponse result = response.getBody();
            log.debug("result : {}", result);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                String userId = response.getBody().getItems().getItems().getId();
                return userId;
            }

        } catch (Exception e){

            log.error(e.getMessage());
            throw new CustomException(UserException.USER_AUTHORIZE_EXCEPTION);
        }
        return null;
    }

    private static final List<FilterTarget> FILTERED_TARGETS = List.of(
            new FilterTarget("POST", "/api/v2/groups"),
            new FilterTarget("POST", "/api/v2/groups/*/applications"),
            new FilterTarget("DELETE", "/api/v2/groups/*/applications"),
            new FilterTarget("POST", "/api/v2/groups/*/join"),
            new FilterTarget("POST", "/api/v2/groups/*/replies"),
            new FilterTarget("POST", "/api/v2/groups/*/replies/*"),
            new FilterTarget("DELETE", "/api/v2/groups/*/replies/*"),
            new FilterTarget("PATCH", "/api/v2/bookmark/")

//            "/test",
//            "/v1/group1",
//            "/test2",
//            "/v2/groups"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return false;
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//
//        return FILTERED_TARGETS.stream()
//                .noneMatch(target ->
//                        target.getMethod().equalsIgnoreCase(method)
//                                && pathMatcher.match(target.getPath(), uri)
//                );
    }
}
