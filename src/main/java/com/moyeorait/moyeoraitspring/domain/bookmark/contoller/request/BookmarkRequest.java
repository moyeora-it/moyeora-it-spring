package com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@ToString
public class BookmarkRequest {
    Long groupId;

    @JsonProperty("isBookmark")
    boolean isBookmark;
}
