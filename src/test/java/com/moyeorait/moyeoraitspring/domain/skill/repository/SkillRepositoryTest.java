package com.moyeorait.moyeoraitspring.domain.skill.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SkillRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SkillRepository skillRepository;

    @Test
    @DisplayName("group을 기준으로 skill리스트를 저장하고, 조회합니다.")
    void saveSkillSuccess(){
        Group group = createGroupInstance("title", "content", 1L, "type");

        groupRepository.save(group);

        List<String> skills = Arrays.asList("JAVA", "PYTHON");
        List<Skill> skillEntities = skills.stream()
                .map(skillName -> new Skill(group, skillName))
                .toList();

        skillRepository.saveAll(skillEntities);

        List<Skill> results = skillRepository.findByGroup(group);
        Assertions.assertThat(results.size()).isEqualTo(skills.size());
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