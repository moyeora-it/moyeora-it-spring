package com.moyeorait.moyeoraitspring.commons.response;

public record ApiPageResponse<T>(
        Status status,
        T items,
        Boolean hasNext,
        Long cursor
) {
    public static <T> ApiPageResponse<T> success(T data, Boolean hasNext, Long cursor){
        return new ApiPageResponse<>(
                new Status(true, 200, "success"),
                data,
                hasNext,
                cursor
        );
    }



}
