package com.moyeorait.moyeoraitspring.domain.group.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class CreateGroupRequest {
    String title;
    LocalDateTime deadLine;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer maxParticipants;
    String description;
    List<Integer> position;
    List<Integer> skills;
    String type;
    boolean autoAllow;

}
