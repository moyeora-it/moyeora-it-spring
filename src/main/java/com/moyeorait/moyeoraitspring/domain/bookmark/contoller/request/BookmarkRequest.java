package com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@ToString
public class BookmarkRequest {
    Long groupId;
    boolean isBookmark;
}
