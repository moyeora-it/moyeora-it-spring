package com.moyeorait.moyeoraitspring.domain.group.service;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupServiceTest {

    @Autowired
    GroupService groupService;

    @Test
    @DisplayName("requestdto와 userID로 그룹 생성합니다..")
    void createGroupSuccess(){
        CreateGroupRequest request = new CreateGroupRequest(
                "스터디 모집합니다",
                LocalDateTime.of(2025, 6, 10, 23, 59),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                LocalDateTime.of(2025, 8, 15, 0, 0),
                5,
                "백엔드 중심의 스터디입니다.",
                Arrays.asList("백엔드", "프론트엔드"),
                Arrays.asList("Java", "Spring", "React"),
                "스터디",
                true
        );

        Long userId = 1L;

        Group result = groupService.createGroup(request, userId);

        Assertions.assertThat(result.getTitle()).isEqualTo(request.getTitle());
    }
}