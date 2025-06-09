package com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BookmarkListRequest {
    List<Long> groupIds;
}
