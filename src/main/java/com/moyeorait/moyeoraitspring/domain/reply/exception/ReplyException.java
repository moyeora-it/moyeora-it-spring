package com.moyeorait.moyeoraitspring.domain.reply.exception;

import com.moyeorait.moyeoraitspring.commons.exception.ExceptionInterface;
import lombok.Getter;

@Getter
public enum ReplyException implements ExceptionInterface {
    REPLY_NOT_FOUND(42001, "존재하지 않는 댓글입니다.");
    ;

    private final int code;
    private final String message;

    ReplyException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
