package com.moyeorait.moyeoraitspring.domain.reply.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReplySaveRequest {

    private Long groupId;
    private Long userId;
    private Long parentId;
    private String content;


}
