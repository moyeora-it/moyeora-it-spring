package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.commons.config.JpaConfig;
import com.moyeorait.moyeoraitspring.domain.user.repository.User;
import com.moyeorait.moyeoraitspring.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
@Import(JpaConfig.class)
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("스터디, 모임 생성 및 조회")
    void createGroup(){
        User user = new User();
        user.setNickname("test");
        user.setIsDeleted(false);
        User saved = userRepository.save(user);

        Group group = new Group();
        group.setTitle("테스트모임");
        group.setDeadline(LocalDateTime.now());
        group.setStartDate(LocalDateTime.now());
        group.setContent("테스트내용");
        group.setEndDate(LocalDateTime.now());
        group.setMaxParticipants(3);
        group.setViews(4);
        group.setType("타입");
        group.setStatus(true);
        group.setAutoAllow(true);
        group.setFirstUserId(saved);


        Group saveGroup = groupRepository.save(group);

        Group findGroup = groupRepository.findById(saveGroup.getId()).get();

        Assertions.assertThat(saveGroup.getTitle()).isEqualTo(findGroup.getTitle());
    }



}