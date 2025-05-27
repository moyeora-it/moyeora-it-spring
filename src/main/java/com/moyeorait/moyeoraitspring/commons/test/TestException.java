package com.moyeorait.moyeoraitspring.commons.test;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;
import lombok.Getter;

@Getter
public enum TestException implements ExceptionInterface {
    TEST(40000, "testException"),
    UNAUTHORIZE(40001, "로그인이 필요합니다.")
    ;

    private final int code;
    private final String message;

    TestException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
