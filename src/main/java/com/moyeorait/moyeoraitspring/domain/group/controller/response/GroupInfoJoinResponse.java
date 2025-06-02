package com.moyeorait.moyeoraitspring.domain.group.controller.response;

import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GroupInfoJoinResponse {

    GroupInfo groupInfo;
    UserInfo userInfo;
    boolean isApplicant;

    public static GroupInfoJoinResponse of(GroupInfo group, UserInfo user, boolean isApplicant) {
        return new GroupInfoJoinResponse(group, user, isApplicant);
    }
}
