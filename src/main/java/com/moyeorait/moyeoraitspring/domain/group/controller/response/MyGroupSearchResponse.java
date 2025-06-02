package com.moyeorait.moyeoraitspring.domain.group.controller.response;

import lombok.*;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyGroupSearchResponse {
    private Long cursor;
    private boolean hasNext;
    List<GroupInfoResponse> items;
}
