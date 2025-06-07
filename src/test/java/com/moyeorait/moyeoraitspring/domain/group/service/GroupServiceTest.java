package com.moyeorait.moyeoraitspring.domain.group.service;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupPagingResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.MyGroupSearchResponse;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupQueryRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.MyGroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class GroupServiceTest {

    @Autowired
    GroupService groupService;
    @Autowired
    WaitingListRepository waitingListRepository;
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupQueryRepository groupQueryRepository;
    @Autowired
    UserManager userManager;
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public UserManager userManager() {
            return Mockito.mock(UserManager.class);
        }
    }

    @PersistenceContext
    EntityManager entityManager;

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
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3),
                "스터디",
                true
        );

        Long userId = 1L;

        Long result = groupService.createGroup(request, userId);
        Group saveGroup = groupService.findById(result);
        Assertions.assertThat(saveGroup.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    @DisplayName("마이페이지 그룹 검색 조회")
    void searchMyGroupSuccess(){
        CreateGroupRequest request = new CreateGroupRequest(
                "스터디 모집합니다1",
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

        CreateGroupRequest request1 = new CreateGroupRequest(
                "스터디 모집합니다2",
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

        CreateGroupRequest request2 = new CreateGroupRequest(
                "스터디 모집합니다3",
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

        UserInfo userInfo = UserInfo.builder()
                .userId(1L)
                .email("test")
                .profileImage("test")
                .nickname("test").build();
        BDDMockito.given(userManager.findNodeUser(1L)).willReturn(userInfo);

        Long userId = 1L;
        groupService.createGroup(request, userId);
        groupService.createGroup(request1, userId);
        groupService.createGroup(request2, userId);

        GroupSearchCondition condition = GroupSearchCondition.builder()
                .skill(Arrays.asList("JavaScript"))
                .keyword("1")
                .cursor(0L)
                .size(10)
                .build();
        MyGroupSearchCondition myCondition = MyGroupSearchCondition.builder()
                .condition(condition)
                .userId(userId)
                .build();
        MyGroupSearchResponse result = groupService.searchMyGroups(myCondition);
        Assertions.assertThat(result.getItems().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("그룹Id로 그룹을 삭제합니다.")
    void deleteGroupSuccess(){
        Long createdUserId = 1L;
        Long joinUserId = 2L;

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

        Long saveGroupId = groupService.createGroup(request, createdUserId);
        Group saveGroup = groupService.findById(saveGroupId);
        ReplySaveRequest replyRequest = new ReplySaveRequest(saveGroup.getGroupId(), joinUserId, null, "테스트 진행");
        Reply reply = Reply.of(replyRequest, saveGroup, null);
        Reply saveReply = replyRepository.save(reply);

        ReplySaveRequest reReplyRequest = new ReplySaveRequest(saveGroup.getGroupId(), joinUserId, saveReply.getReplyId(), "테스트 진행");
        Reply reReply = Reply.of(replyRequest, saveGroup, saveReply);
        Reply saveReReply = replyRepository.save(reReply);

        entityManager.flush();
        entityManager.clear();

        groupRepository.deleteById(saveGroup.getGroupId());
        Optional<Group> group = groupRepository.findById(saveGroup.getGroupId());
        Assertions.assertThat(group).isEmpty();
    }

    @Test
    @DisplayName("그룹 조회를 진행합니다.")
    void recommendGroupSelect(){
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

        CreateGroupRequest request2 = new CreateGroupRequest(
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
        Long userId = 1L;
        groupService.createGroup(request, userId);
        groupService.createGroup(request2, userId);

        GroupSearchCondition condition = GroupSearchCondition.builder()
                .keyword("스터디")
                .cursor(0L)
                .size(10000)
                .build();


        GroupPagingResponse result = groupService.searchGroups(condition, null);
        for(GroupInfo groupInfoResponse : result.getItems()){
            System.out.println("groupInfoResponse : "+ groupInfoResponse);
        }
        System.out.println("size" + result.getItems().size());
    }

}