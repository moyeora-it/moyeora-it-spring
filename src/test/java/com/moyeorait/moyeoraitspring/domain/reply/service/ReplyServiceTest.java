package com.moyeorait.moyeoraitspring.domain.reply.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    GroupRepository groupRepository;

    @PersistenceContext
    EntityManager entityManager;
    @Test
    @DisplayName("댓글을 삭제한다.")
    void replyDeleteSuccess(){
        Group group = createGroupInstance("title", "content", 1L, "type");
        Group saveGroup = groupRepository.save(group);

        entityManager.flush();
        entityManager.clear();




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