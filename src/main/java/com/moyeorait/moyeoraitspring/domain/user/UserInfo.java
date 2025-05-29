package com.moyeorait.moyeoraitspring.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@Builder
public class UserInfo {
    Long userId;
    String nickname;
    String profileImage;
    String email;

    public static UserInfo from(UserNodeResponse response) {

        return UserInfo.builder()
                .userId(response.getUserId())
                .nickname(response.getNickname())
                .profileImage(response.getProfileImage())
                .email(response.getEmail())
                .build();
    }
}
