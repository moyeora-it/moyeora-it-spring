package com.moyeorait.moyeoraitspring.commons.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final ExceptionInterface exception;

    public NotFoundException(ExceptionInterface exception) {
        super(exception.getMessage());
        this.exception = exception;
    }
}
