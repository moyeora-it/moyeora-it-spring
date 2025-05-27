package com.moyeorait.moyeoraitspring.domain.group.controller.request;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class CreateGroupRequest {
    String title;
    LocalDateTime deadLine;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer maxParticipants;
    String description;
    List<String> position;
    List<String> skills;
    List<String> type;
    boolean autoAllow;

}
