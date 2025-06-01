package com.moyeorait.moyeoraitspring.domain.reply.controller.request;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReplySearchRequest {
    private Long cursor;
    private int size;
}
