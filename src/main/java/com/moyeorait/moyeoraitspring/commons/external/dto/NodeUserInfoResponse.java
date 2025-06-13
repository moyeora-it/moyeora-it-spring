package com.moyeorait.moyeoraitspring.commons.external.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeUserInfoResponse {
    private NodeStatus status;
    private NodeUserInfo data;
}