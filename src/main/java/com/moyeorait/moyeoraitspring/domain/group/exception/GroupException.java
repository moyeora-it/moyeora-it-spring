package com.moyeorait.moyeoraitspring.domain.group.exception;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;
import lombok.Getter;

@Getter
public enum GroupException implements ExceptionInterface {
    GROUP_CAPACITY_EXCEEDED(41001, "모임의 최대 인원을 초과하였습니다."),
    PARTICIPATION_NOT_FOUND(41002, "해당 유저의 참여 신청 정보가 존재하지 않습니다."),
    USER_FORBIDDEN_ACCESS(41003, "권한이 없는 유저입니다.")
    ;

    private final int code;
    private final String message;

    GroupException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
