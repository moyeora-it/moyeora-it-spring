//package com.moyeorait.moyeoraitspring.commons.config;
//
//import com.moyeorait.moyeoraitspring.commons.annotation.LoginUserArgumentResolver;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
////@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    private final LoginUserArgumentResolver loginUserArgumentResolver;
//
//    public WebConfig(LoginUserArgumentResolver resolver) {
//        this.loginUserArgumentResolver = resolver;
//    }
//
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(loginUserArgumentResolver);
//    }
//}
