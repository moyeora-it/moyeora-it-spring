package com.moyeorait.moyeoraitspring.domain.participant.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class ParticipantServiceTest {

    @Autowired
    ParticipantService participantService;

    @Autowired
    GroupRepository groupRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("유저를 그룹에 추가한다.")
    void addUserToGroupSuccess(){

        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);

        entityManager.flush();
        entityManager.clear();

        Group findGroup = groupRepository.findById(saveGroup.getGroupId()).get();
        Long joinUserId = 101L;
        participantService.addUserToGroup(findGroup, joinUserId);

        entityManager.flush();
        entityManager.clear();

        Group result = groupRepository.findById(findGroup.getGroupId()).get();

        Assertions.assertThat(result.getCurrentParticipants()).isEqualTo(1);

    }

    @Test
    @DisplayName("Group과 userId를 기반으로 현재 참여 정보를 조회한다.")
    void selectParticipantInfoByUserIdAndGroup(){
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);
        Long joinUserId = 101L;
        participantService.addUserToGroup(saveGroup, joinUserId);

        entityManager.flush();
        entityManager.clear();

        Group findGroup = groupRepository.findById(saveGroup.getGroupId()).get();
        Participant participant = participantService.findByGroupAndUserId(findGroup, joinUserId);

        Assertions.assertThat(participant).isNotNull();
    }

    @Test
    @DisplayName("참여자 정보를 삭제한다.")
    void deleteParticipantSuccess(){
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);
        Long joinUserId = 101L;
        participantService.addUserToGroup(saveGroup, joinUserId);

        entityManager.flush();
        entityManager.clear();

        Group findGroup = groupRepository.findById(saveGroup.getGroupId()).get();
        Participant participant = participantService.findByGroupAndUserId(findGroup, joinUserId);

        participantService.deleteParticipant(participant);
        Participant result = participantService.findByGroupAndUserId(findGroup, joinUserId);
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