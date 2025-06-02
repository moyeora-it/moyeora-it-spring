package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;
import lombok.Getter;

@Getter
public enum UserException implements ExceptionInterface {
    USER_AUTHORIZE_EXCEPTION(40001, "인증이 불가능한 토큰정보입니다.");
    ;


    private final int code;
    private final String message;

    UserException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
