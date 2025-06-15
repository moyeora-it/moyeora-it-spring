package com.moyeorait.moyeoraitspring.domain.bookmark;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.exception.NotFoundException;
import com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request.BookmarkListRequest;
import com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request.BookmarkRequest;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.Bookmark;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.BookmarkRepository;
import com.moyeorait.moyeoraitspring.domain.group.exception.GroupException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookmarkService {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    GroupRepository groupRepository;

    public void updateBookmark(BookmarkRequest request, Long userId) {
        Group findgroup = groupRepository.findById(request.getGroupId()).get();
        log.debug("findGroup : {}", findgroup);
        log.debug("request : {}", request);
        if(request.isBookmark()){
            if(bookmarkRepository.existsByGroupAndUserId(findgroup, userId)) return;
            Bookmark bookmark = new Bookmark(findgroup, userId);
            bookmarkRepository.save(bookmark);
        }else{
            if(!bookmarkRepository.existsByGroupAndUserId(findgroup, userId)) return;
            Bookmark bookmark = bookmarkRepository.findByGroupAndUserId(findgroup, userId);
            bookmarkRepository.delete(bookmark);
        }
    }

    public void addBookmarks(BookmarkListRequest request, Long userId) {
        List<Long> bookmarks = request.getGroupIds();

        for(Long groupId : bookmarks){
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException(GroupException.GROUP_NOT_FOUND));

            boolean exists = bookmarkRepository.existsByGroupAndUserId(group, userId);
            if (!exists) {
                Bookmark bookmark = new Bookmark(group, userId);
                bookmarkRepository.save(bookmark);
            }
        }
        log.debug("유저 {}에 대해 {}개의 북마크가 추가되었습니다.", userId, bookmarks.size());
    }
}
