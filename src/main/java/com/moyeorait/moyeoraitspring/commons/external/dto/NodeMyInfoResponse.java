package com.moyeorait.moyeoraitspring.commons.external.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeMyInfoResponse {
    private boolean success;
    private NodeItemsWrapper items;
}