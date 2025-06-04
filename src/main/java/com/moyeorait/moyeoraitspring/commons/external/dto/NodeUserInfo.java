package com.moyeorait.moyeoraitspring.commons.external.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NodeUserInfo {
    private String id;
    private String email;
    private String nickname;
    private String profile_image;
    private double averageRating;
}
