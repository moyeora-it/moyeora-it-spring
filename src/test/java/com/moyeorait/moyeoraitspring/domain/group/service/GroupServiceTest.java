package com.moyeorait.moyeoraitspring.domain.group.service;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupQueryRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
                Arrays.asList("백엔드", "프론트엔드"),
                Arrays.asList("Java", "Spring", "React"),
                "스터디",
                true
        );

        Long userId = 1L;

        Group result = groupService.createGroup(request, userId);

        Assertions.assertThat(result.getTitle()).isEqualTo(request.getTitle());
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
                Arrays.asList("백엔드", "프론트엔드"),
                Arrays.asList("Java", "Spring", "React"),
                "스터디",
                true
        );

        Group saveGroup = groupService.createGroup(request, createdUserId);

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
        GroupSearchCondition condition = GroupSearchCondition.builder()
                .build();

        List<GroupInfoResponse> result = groupService.searchGroups(condition);
        for(GroupInfoResponse groupInfoResponse : result){
            System.out.println("groupInfoResponse : "+ groupInfoResponse);
        }
    }

}