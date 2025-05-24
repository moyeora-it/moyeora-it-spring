package com.moyeorait.moyeoraitspring.commons.test;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;
import lombok.Getter;

@Getter
public enum TestException implements ExceptionInterface {
    TEST(40000, "testException")

    ;

    private final int code;
    private final String message;

    TestException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
