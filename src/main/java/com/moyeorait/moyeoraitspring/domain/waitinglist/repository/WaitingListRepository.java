package com.moyeorait.moyeoraitspring.domain.waitinglist.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {

    int deleteByGroupAndUserId(Group group, Long currentUserId);

    WaitingList findByGroupAndUserId(Group group, Long userId);

    void deleteByGroup(Group group);
}

