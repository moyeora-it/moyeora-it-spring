package com.moyeorait.moyeoraitspring.domain.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NodeFollowerListResponse {
    private Status status;
    private List<FollowerInfo> items;
    private String cursor;
    private boolean hasNext;
    private int totalCount;

    @Getter
    public static class Status {
        private boolean success;
    }
}