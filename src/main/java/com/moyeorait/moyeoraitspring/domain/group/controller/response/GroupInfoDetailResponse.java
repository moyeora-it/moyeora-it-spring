package com.moyeorait.moyeoraitspring.domain.group.controller.response;

import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GroupInfoDetailResponse {


    GroupInfo groupInfo;
    UserInfo userInfo;
    boolean isApplicant;
    boolean isJoined;
    boolean isBookmark;


}
