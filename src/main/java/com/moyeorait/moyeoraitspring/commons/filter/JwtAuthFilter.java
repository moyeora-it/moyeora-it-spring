package com.moyeorait.moyeoraitspring.commons.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import com.moyeorait.moyeoraitspring.domain.user.exception.UserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String JWT_AUTH_URL = "https://my-api.sjcpop.com/api/v1/user/info";
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserManager userManager;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if(token == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.debug("토큰이 없습니다.");
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

        String userId = null;
        try {
            userId = userManager.findUserInfoByTokenAndNode(token);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> apiResponse = ApiResponse.fail(UserException.USER_AUTHORIZE_EXCEPTION);
            String json = objectMapper.writeValueAsString(apiResponse);

            response.getWriter().write(json);
            return;
        }
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
        // 2. 쿠키에서 accessToken 추출
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    log.debug("accessToken from cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        log.debug("토큰이 없습니다. (Authorization, Cookie 모두 없음)");
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
