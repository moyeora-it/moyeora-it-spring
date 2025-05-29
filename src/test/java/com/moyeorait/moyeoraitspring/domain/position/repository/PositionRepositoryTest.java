package com.moyeorait.moyeoraitspring.domain.position.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.skill.repository.SkillRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PositionRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PositionRepository positionRepository;

    @Test
    @DisplayName("group을 기준으로 position리스트를 저장하고, 조회합니다.")
    void savePositionSuccess(){
        Group group = createGroupInstance("title", "content", 1L, "type");

        Group saveGroup = groupRepository.save(group);

        List<String> positions = Arrays.asList("BE", "FE");
        List<Position> positionEntities = positions.stream()
                .map(positionInfo -> new Position(saveGroup, positionInfo))
                .toList();

        positionRepository.saveAll(positionEntities);

        List<Position> result = positionRepository.findByGroup(group);
        Assertions.assertThat(result.size()).isEqualTo(positions.size());
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