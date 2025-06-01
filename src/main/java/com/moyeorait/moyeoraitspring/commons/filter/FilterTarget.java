package com.moyeorait.moyeoraitspring.commons.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterTarget {
    private final String method;
    private final String path;
}
