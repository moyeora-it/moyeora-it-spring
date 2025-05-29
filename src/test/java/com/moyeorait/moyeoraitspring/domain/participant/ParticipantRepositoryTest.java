package com.moyeorait.moyeoraitspring.domain.participant;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
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
class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    GroupRepository groupRepository;

    @Test
    @DisplayName("참여자로 등록한다.")
    void resisterUserToGroup(){
        //given
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);
        Long userId = 101L;

        Participant participant = new Participant(saveGroup, userId);
        Participant saveParticipant = participantRepository.save(participant);

        //when
        Participant result = participantRepository.findByGroupAndUserId(group, userId);

        //then
        Assertions.assertThat(saveParticipant).isEqualTo(result);
    }
    @Test
    @DisplayName("참여자 목록에서 삭제한다.")
    void deleteUserToGroup(){
        //given
        Group group = createGroupInstance("title", "content", 100L, "type");
        Group saveGroup = groupRepository.save(group);
        Long userId = 101L;

        Participant participant = new Participant(saveGroup, userId);
        Participant saveParticipant = participantRepository.save(participant);

        int result = participantRepository.deleteByGroupAndUserId(saveGroup, userId);

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