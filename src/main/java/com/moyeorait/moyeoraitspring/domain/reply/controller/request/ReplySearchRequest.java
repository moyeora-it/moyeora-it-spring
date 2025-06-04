package com.moyeorait.moyeoraitspring.domain.reply.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ReplySearchRequest {
    private Long cursor;
    private int size;
}
