package com.moyeorait.moyeoraitspring.commons.response;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;

public record ApiResponse<T> (
        Status status,
        T items
){


    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(new Status(true, 200, "success"), null);
    }
    public static <T> ApiResponse<T> success(T items){
        return new ApiResponse<>(new Status(true, 200, "success"), items);
    }

    public static <T> ApiResponse<T> fail(ExceptionInterface exception){
        return new ApiResponse<>(Status.fail(exception), null);
    }

}
