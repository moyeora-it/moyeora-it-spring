package com.moyeorait.moyeoraitspring.domain.reply.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReplyUpdateRequest {
    private Long groupId;
    private Long userId;
    private Long replyId;
    private String content;
}
