package com.moyeorait.moyeoraitspring.domain.group.repository.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyGroupSearchCondition {

    private GroupSearchCondition condition;
    private String status;
    private Integer size;
    private Long cursor;
    private Long userId;
}
