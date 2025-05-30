package com.moyeorait.moyeoraitspring.domain.bookmark;

import com.moyeorait.moyeoraitspring.domain.bookmark.contoller.request.BookmarkRequest;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.Bookmark;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.BookmarkRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    GroupRepository groupRepository;

    public void updateBookmark(BookmarkRequest request, Long userId) {
        Group findgroup = groupRepository.findById(request.getGroupId()).get();
        if(request.isBookmark()){
            Bookmark bookmark = new Bookmark(findgroup, userId);
            bookmarkRepository.save(bookmark);
        }else{
            Bookmark bookmark = bookmarkRepository.findByGroupAndUserId(findgroup, userId);
            bookmarkRepository.delete(bookmark);
        }
    }
}
