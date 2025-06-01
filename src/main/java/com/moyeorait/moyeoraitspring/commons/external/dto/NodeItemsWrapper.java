package com.moyeorait.moyeoraitspring.commons.external.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeItemsWrapper {
    private NodeUserInfo items;
    private double averageRating;
}
