package com.moyeorait.moyeoraitspring.domain.group.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GroupInfoJoinResponse {


    GroupInfo group;
    UserInfo host;
    @JsonProperty("isApplicant")
    boolean applicant;
    @JsonProperty("isJoined")
    boolean joined;
    public static GroupInfoJoinResponse of(GroupInfo group, UserInfo user, boolean isApplicant, boolean isJoined) {
        return new GroupInfoJoinResponse(group, user, isApplicant, isJoined);
    }
}
