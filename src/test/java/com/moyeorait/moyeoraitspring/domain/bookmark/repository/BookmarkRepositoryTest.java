package com.moyeorait.moyeoraitspring.domain.bookmark.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookmarkRepositoryTest {

    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    GroupRepository groupRepository;
    @PersistenceContext
    EntityManager entityManager;


    @Test
    @DisplayName("북마크를 추가한다.")
    void addBookmarkSuccess(){
        Group group = createGroupInstance("title", "content", 1L, "type");
        Group saveGroup = groupRepository.save(group);

        Long userId = 100L;

        Bookmark bookmark = new Bookmark(group, userId);
        Bookmark saveBookmark = bookmarkRepository.save(bookmark);

    }

    @Test
    @DisplayName("북마크를 삭제한다.")
    void deleteBookmarkSuccess(){
        Group group = createGroupInstance("title", "content", 1L, "type");
        Group saveGroup = groupRepository.save(group);

        Long userId = 100L;
        Bookmark bookmark = new Bookmark(group, userId);
        Bookmark saveBookmark = bookmarkRepository.save(bookmark);

        entityManager.flush();
        entityManager.clear();

        Group findGroup = groupRepository.findById(saveGroup.getGroupId()).get();
        Bookmark findBookmark = bookmarkRepository.findByGroupAndUserId(findGroup, userId);
        bookmarkRepository.delete(findBookmark);

        Bookmark result = bookmarkRepository.findByGroupAndUserId(findGroup, userId);
        Assertions.assertThat(result).isNull();
    }

    private Group createGroupInstance(String title, String content, Long userId, String type){
        return Group.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .autoAllow(true)
                .currentParticipants(0)
                .maxParticipants(10)
                .status(true)
                .type(type)
                .views(0)
                .deadline(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
    }
}