package com.moyeorait.moyeoraitspring.domain.user.dto;

import lombok.Getter;

@Getter
public class FollowerInfo {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private boolean isFollower;
    private boolean isFollowing;
}