package com.moyeorait.moyeoraitspring.domain.group.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GroupSearchRequest {
    private String sort;
    private String order;
    private List<Integer> skill;
    private List<Integer> position;
    private String type;
    private String search;
    @NotNull(message = "size는 필수입니다.")
    private Integer size;
    private Long cursor;
}
