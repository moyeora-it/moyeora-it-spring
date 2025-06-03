package com.moyeorait.moyeoraitspring.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final boolean success;
    private final int code;
    private final String message;
}
