package com.moyeorait.moyeoraitspring.domain.group.service.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class GroupInfo {
    Long id;
    String title;
    LocalDateTime deadline;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer maxParticipants;
    List<UserInfo> participants;
    String description;
    List<Integer> position;
    List<Integer> skills;
    LocalDateTime createdAt;
    @JsonProperty("isBookmark")
    boolean bookmark;
    boolean autoAllow;
    String type;
    Long userId;

    
//    Integer currentParticipants;
//    boolean status;
//    Integer views;

    public static GroupInfo of(Group group, List<Integer> skills, List<Integer> positions) {
        return GroupInfo.builder()
                .id(group.getGroupId())
                .title(group.getTitle())
                .deadline(group.getDeadline())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .maxParticipants(group.getMaxParticipants())
                .description(group.getContent())
                .position(positions)
                .skills(skills)
                .createdAt(group.getCreatedAt())
                .autoAllow(group.getAutoAllow())
                .type(group.getType())
                .userId(group.getUserId())
//                .currentParticipants(group.getCurrentParticipants())
//                .status(group.getStatus())
//                .views(group.getViews())
                .build();
    }
    public static GroupInfo of(Group group, List<Integer> skills, List<Integer> positions, List<UserInfo> users, boolean isBookmark) {
        return GroupInfo.builder()
                .id(group.getGroupId())
                .title(group.getTitle())
                .description(group.getContent())
                .userId(group.getUserId())
                .autoAllow(group.getAutoAllow())
//                .currentParticipants(group.getCurrentParticipants())
                .maxParticipants(group.getMaxParticipants())
//                .status(group.getStatus())
                .type(group.getType())
//                .views(group.getViews())
                .deadline(group.getDeadline())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .skills(skills)
                .position(positions)
                .participants(users)
                .createdAt(group.getCreatedAt())
                .bookmark(isBookmark)
                .build();
    }
}
