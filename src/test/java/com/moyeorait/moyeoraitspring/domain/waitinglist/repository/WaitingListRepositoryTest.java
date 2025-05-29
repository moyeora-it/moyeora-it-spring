package com.moyeorait.moyeoraitspring.domain.waitinglist.repository;

import com.moyeorait.moyeoraitspring.MoyeoraItSpringApplication;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
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
class WaitingListRepositoryTest {

    @Autowired
    WaitingListRepository waitingListRepository;

    @Autowired
    GroupRepository groupRepository;

    @Test
    @DisplayName("그룹 참여를 요청하고, 해당 그룹 대기열에 추가한다.")
    void requestJoinGroupAndSaveWaitinglist(){
        //given
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);

        Long currentUserId = 101L;

        WaitingList waitingList = new WaitingList(saveGroup, currentUserId);
        WaitingList saveWaitingList = waitingListRepository.save(waitingList);

        //when
        WaitingList result = waitingListRepository.findById(saveWaitingList.getWaitingListId()).get();

        //then
        Assertions.assertThat(result).isEqualTo(saveWaitingList);
    }

    @Test
    @DisplayName("참여를 요청했던 그룹에서, 대기하던 중 요청을 취소한다.")
    void requestCancleJoinGroup(){
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);
        Long currentUserId = 101L;
        WaitingList waitingList = new WaitingList(saveGroup, currentUserId);
        WaitingList saveWaitingList = waitingListRepository.save(waitingList);

        int result = waitingListRepository.deleteByGroupAndUserId(group, currentUserId);
        Assertions.assertThat(result).isEqualTo(1);
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