package com.moyeorait.moyeoraitspring.domain.bookmark.contoller;

import com.moyeorait.moyeoraitspring.commons.annotation.Login;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.bookmark.BookmarkService;
import com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request.BookmarkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/bookmark")
public class BookmarkController {

    @Autowired
    BookmarkService bookmarkService;

    @PatchMapping
    public ApiResponse<Void> patchBookmark(@Login Long userId, @RequestBody BookmarkRequest request) {

        bookmarkService.updateBookmark(request, userId);

        return ApiResponse.success();
    }

}
