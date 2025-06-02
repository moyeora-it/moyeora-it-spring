package com.moyeorait.moyeoraitspring.commons.response;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;

public record Status(
        boolean success,
        int code,
        String message
){
    public static Status fail(ExceptionInterface exception){
        return new Status(false, exception.getCode(), exception.getMessage());
    }
}
