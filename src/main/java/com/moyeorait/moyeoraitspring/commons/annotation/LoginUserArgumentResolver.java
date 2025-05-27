//package com.moyeorait.moyeoraitspring.commons.annotation;
//
//import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
//import com.moyeorait.moyeoraitspring.commons.test.TestException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.core.MethodParameter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
////@Component
//public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.hasParameterAnnotation(Login.class) &&
//                parameter.getParameterType().isAssignableFrom(Integer.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
//        String userIdStr = request.getParameter("userId");
//        if(userIdStr != null) throw new CustomException(TestException.UNAUTHORIZE);
//        return Integer.parseInt(userIdStr);
//    }
//}
