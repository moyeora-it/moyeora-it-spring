package com.moyeorait.moyeoraitspring.domain.group.controller.response;

import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GroupInfoResponse {

    GroupInfo group;

    public static GroupInfoResponse of(GroupInfo group) {
        return new GroupInfoResponse(group);
    }
}
