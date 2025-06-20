package com.moyeorait.moyeoraitspring.domain.group.repository.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class GroupSearchCondition {

    private String keyword; // 검색어
    private String type; // study, project, all
    private List<String> skill;
    private List<String> position;
    private String sort;  // start_date, end_date, deadline
    private String order; // asc, desc
    private Integer size;
    private Long cursor;
    private boolean bookmark;
    private Long bookmarkUserId;
}
