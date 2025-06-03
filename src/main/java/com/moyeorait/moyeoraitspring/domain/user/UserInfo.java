package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse2;
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

    public static UserInfo of(NodeUserInfoResponse2 response, Long userId) {

        return UserInfo.builder()
                .userId(userId)
                .nickname(response.getUser().getItems().getNickname())
                .profileImage(response.getUser().getItems().getProfile_image())
                .email(response.getUser().getItems().getEmail())
                .build();
    }
}
