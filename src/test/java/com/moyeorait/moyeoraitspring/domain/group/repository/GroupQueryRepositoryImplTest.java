package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupQueryRepositoryImplTest {

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @Autowired
    GroupRepository groupRepository;

    @Test
    @DisplayName("조회 조건에 따라 Group엔티티를 반환한다.")
    void groupSearchSuccess(){
        Group group1 = createGroupInstance("test1", "testcon1", 1L, "testType");
        Group group2 = createGroupInstance("testTitle2", "testContent2", 1L, "STUDY");
        Group group3 = createGroupInstance("testTitle3", "testContent3", 1L, "STUDY");
        Group group4 = createGroupInstance("testTitle4", "testContent4", 1L, "STUDY");
        Group group5 = createGroupInstance("testTitle5", "testContent5", 1L, "STUDY");

        groupRepository.save(group1);
        groupRepository.save(group2);
        groupRepository.save(group3);
        groupRepository.save(group4);
        groupRepository.save(group5);

        GroupSearchCondition condition = GroupSearchCondition.builder()
                .keyword("Title")
                .type("STUDY")
                .size(10000)
                .cursor(0L)
                .build();

        List<Group> result = groupQueryRepository.searchGroup(condition);
        Assertions.assertThat(result.size()).isEqualTo(4);

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