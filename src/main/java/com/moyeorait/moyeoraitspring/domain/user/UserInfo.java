package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfo;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

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
        NodeUserInfo user = response.getData();

        return UserInfo.builder()
                .userId(userId)
                .nickname(Optional.ofNullable(user.getNickname()).orElse(""))
                .profileImage(Optional.ofNullable(user.getProfile_image()).orElse(""))
                .email(Optional.ofNullable(user.getEmail()).orElse(""))
                .build();
    }
}
