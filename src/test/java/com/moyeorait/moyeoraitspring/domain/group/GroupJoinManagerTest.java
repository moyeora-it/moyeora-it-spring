package com.moyeorait.moyeoraitspring.domain.group;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.participant.service.ParticipantService;
import com.moyeorait.moyeoraitspring.domain.waitinglist.service.WaitingListService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupJoinManagerTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    WaitingListService waitingListService;

    @Autowired
    GroupService groupService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    GroupJoinManager groupJoinManager;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("사용자가 그룹 참여를 취소한다.")
    void cancleCandidateSuccess(){
        Long userId = 1L;
        Long participantUserId = 2L;
        CreateGroupRequest request = new CreateGroupRequest(
                "스터디 모집합니다",
                LocalDateTime.of(2025, 6, 10, 23, 59),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                LocalDateTime.of(2025, 8, 15, 0, 0),
                5,
                "백엔드 중심의 스터디입니다.",
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3),
                "스터디",
                true
        );
        Long saveGroupId = groupService.createGroup(request, userId);
        Group savetGroup = groupService.findById(saveGroupId);

        Participant participant = new Participant(savetGroup, participantUserId);
        participantRepository.save(participant);

        entityManager.flush();
        entityManager.clear();

        groupJoinManager.cancelRequest(saveGroupId.longValue(), participantUserId.longValue());

        Optional<Participant> findParticipant = participantRepository.findById(participant.getParticipantId());
        Assertions.assertThat(findParticipant).isEmpty();
    }
}