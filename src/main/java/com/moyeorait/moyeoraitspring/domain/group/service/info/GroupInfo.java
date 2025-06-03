package com.moyeorait.moyeoraitspring.domain.group.service.info;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
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
    Long groupId;
    String title;
    String description;
    Long userId;
    boolean autoAllow;
    Integer currentParticipants;
    Integer maxParticipants;
    boolean status;
    String type;
    Integer views;
    List<Integer> skills;
    List<Integer> positions;
    LocalDateTime deadline;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<UserInfo> userInfos;

    public static GroupInfo of(Group group, List<Integer> skills, List<Integer> positions) {
        return GroupInfo.builder()
                .groupId(group.getGroupId())
                .title(group.getTitle())
                .description(group.getContent())
                .userId(group.getUserId())
                .autoAllow(group.getAutoAllow())
                .currentParticipants(group.getCurrentParticipants())
                .maxParticipants(group.getMaxParticipants())
                .status(group.getStatus())
                .type(group.getType())
                .views(group.getViews())
                .deadline(group.getDeadline())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .skills(skills)
                .positions(positions)
                .build();
    }
    public static GroupInfo of(Group group, List<Integer> skills, List<Integer> positions, List<UserInfo> users) {
        return GroupInfo.builder()
                .groupId(group.getGroupId())
                .title(group.getTitle())
                .description(group.getContent())
                .userId(group.getUserId())
                .autoAllow(group.getAutoAllow())
                .currentParticipants(group.getCurrentParticipants())
                .maxParticipants(group.getMaxParticipants())
                .status(group.getStatus())
                .type(group.getType())
                .views(group.getViews())
                .deadline(group.getDeadline())
                .startDate(group.getStartDate())
                .endDate(group.getEndDate())
                .skills(skills)
                .positions(positions)
                .userInfos(users)
                .build();
    }
}
