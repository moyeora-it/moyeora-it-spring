package com.moyeorait.moyeoraitspring.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNodeResponse {
    Long userId;
    String email;
    String nickname;
    String profileImage;

}
