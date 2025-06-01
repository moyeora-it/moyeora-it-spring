package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
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

    public static UserInfo of(NodeUserInfoResponse response, Long userId) {

        return UserInfo.builder()
                .userId(userId)
                .nickname(response.getItems().getItems().getNickname())
                .profileImage(response.getItems().getItems().getProfile_image())
                .email(response.getItems().getItems().getEmail())
                .build();
    }
}
