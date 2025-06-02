package com.moyeorait.moyeoraitspring.commons.annotation;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.test.TestException;
import com.moyeorait.moyeoraitspring.domain.user.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class) &&
                parameter.getParameterType().isAssignableFrom(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userIdStr = (String) request.getAttribute("userId");
        log.debug("userId : {}", userIdStr);

        Login loginAnnotation = parameter.getParameterAnnotation(Login.class);

        if (userIdStr == null) {
            if (loginAnnotation != null && loginAnnotation.required()) {
                throw new CustomException(UserException.USER_AUTHORIZE_EXCEPTION);
            } else {
                return null;
            }
        }
//        if(userIdStr == null) throw new CustomException(TestException.UNAUTHORIZE);
        if(userIdStr == null) return null;
        return Long.parseLong(userIdStr);
    }
}
