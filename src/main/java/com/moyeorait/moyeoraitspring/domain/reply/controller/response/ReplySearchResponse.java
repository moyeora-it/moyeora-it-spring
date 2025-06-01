package com.moyeorait.moyeoraitspring.domain.reply.controller.response;

import com.moyeorait.moyeoraitspring.domain.reply.service.dto.ReplyInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplySearchResponse {

    List<ReplyInfo> items;
    boolean hasNext;
    Long cursor;
}
