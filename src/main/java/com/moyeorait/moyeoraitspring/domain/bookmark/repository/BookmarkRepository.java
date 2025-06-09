package com.moyeorait.moyeoraitspring.domain.bookmark.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Bookmark findByGroupAndUserId(Group findGroup, Long userId);

    boolean existsByGroupAndUserId(Group group, Long userId);
}
