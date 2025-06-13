package com.moyeorait.moyeoraitspring.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class UserInfoList {
    List<UserInfo> participants;
}
