package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.commons.config.JpaConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;


    @Test
    @DisplayName("스터디, 모임 생성 및 조회")
    void createGroup(){

        // given
        Group group = new Group();
        group.setTitle("테스트모임");
        group.setContent("테스트내용");
        group.setUserId(1L);
        group.setAutoAllow(true);
        group.setCurrentParticipants(0);
        group.setMaxParticipants(3);
        group.setStatus(true);
        group.setType("타입");
        group.setViews(4);
        group.setDeadline(LocalDateTime.now());
        group.setStartDate(LocalDateTime.now());
        group.setEndDate(LocalDateTime.now());

        //when
        Group saveGroup = groupRepository.save(group);
        Group findGroup = groupRepository.findById(saveGroup.getGroupId()).get();

        //then
        Assertions.assertThat(saveGroup.getTitle()).isEqualTo(findGroup.getTitle());
    }

    @Test
    @DisplayName("모임 상세 조회")
    void findGroupInfo(){

    }


}