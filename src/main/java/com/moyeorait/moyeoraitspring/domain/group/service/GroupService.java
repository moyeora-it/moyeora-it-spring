package com.moyeorait.moyeoraitspring.domain.group.service;

import com.moyeorait.moyeoraitspring.commons.enumdata.PositionEnum;
import com.moyeorait.moyeoraitspring.commons.enumdata.SkillEnum;
import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.BookmarkRepository;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoJoinResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.MyGroupSearchResponse;
import com.moyeorait.moyeoraitspring.domain.group.exception.GroupException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupQueryRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.MyGroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.position.repository.Position;
import com.moyeorait.moyeoraitspring.domain.position.repository.PositionRepository;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import com.moyeorait.moyeoraitspring.domain.skill.repository.Skill;
import com.moyeorait.moyeoraitspring.domain.skill.repository.SkillRepository;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import com.moyeorait.moyeoraitspring.domain.user.UserService;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingList;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupQueryRepository groupQueryRepository;
    private final UserService userservice;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    private final UserManager userManager;
    private final ParticipantRepository participantRepository;
    private final WaitingListRepository waitingListRepository;
    private final ReplyRepository replyRepository;
    private final BookmarkRepository bookmarkRepository;


    public Long createGroup(CreateGroupRequest request, Long userId) {
        Group group = new Group(request, userId);
        Group result = groupRepository.save(group);

        Participant participant = new Participant(group, userId);
        participantRepository.save(participant);

        List<String> skills = SkillEnum.createStringList(request.getSkills());

        List<Skill> skillEntities = skills.stream()
                .map(skillInfo -> new Skill(result, skillInfo))
                .toList();

        skillRepository.saveAll(skillEntities);


        List<String> positions = PositionEnum.createStringList(request.getPosition());

        List<Position> positionEntities = positions.stream()
                .map(positionInfo -> new Position(result, positionInfo))
                .toList();

        positionRepository.saveAll(positionEntities);

        return result.getGroupId();
    }

    public GroupInfoJoinResponse findGroupByGroupId(Long groupId, Long loginUserId) {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(
                GroupException.GROUP_NOT_FOUND));

        List<String> skills = skillRepository.findByGroup(group).stream()
                .map(Skill::getSkillInfo)
                .toList();

        List<Integer> skillIndex = SkillEnum.createIdxList(skills);

        List<String> positions = positionRepository.findByGroup(group).stream()
                .map(Position::getPositionInfo)
                .toList();

        List<Integer> positinIndex1 = PositionEnum.createIdxList(positions);

        List<Participant> users = participantRepository.findByGroup(group);
        List<UserInfo> userList = users.stream()
                .map(user -> {
                    long userId = user.getUserId();
                    UserInfo response = userManager.findNodeUser(userId);
                    return response;
                })
                .toList();

        GroupInfo groupInfo = GroupInfo.of(group, skillIndex, positinIndex1, userList);

        Long createdUserId = group.getUserId();
        log.debug("createUserId : {}", createdUserId);
        UserInfo userInfo = userManager.findNodeUser(createdUserId);

        boolean isApplicant = false;
        boolean isJoined = false;
        boolean isBookmark = false;
        if(loginUserId != null){
            if(waitingListRepository.findByGroupAndUserId(group, loginUserId) != null) isApplicant = true;
            if(participantRepository.findByGroupAndUserId(group, loginUserId) != null) {
                isApplicant = true;
                isJoined = true;
            }
            if(bookmarkRepository.findByGroupAndUserId(group, loginUserId) != null) isBookmark = true;
        }



        GroupInfoJoinResponse result = GroupInfoJoinResponse.of(groupInfo, userInfo, isApplicant, isJoined, isBookmark);

        return result;
    }

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId).get();
    }

    public List<GroupInfoResponse> searchGroups(GroupSearchCondition condition) {
        List<Group> groups = groupQueryRepository.searchGroup(condition);

        return groups.stream()
                .map(group -> {
                    // 기술 스택 조회
                    List<String> skills = skillRepository.findByGroup(group).stream()
                            .map(Skill::getSkillInfo)
                            .toList();
                    List<Integer> skillIdx = SkillEnum.createIdxList(skills);
                    // 포지션 조회
                    List<String> positions = positionRepository.findByGroup(group).stream()
                            .map(Position::getPositionInfo)
                            .toList();

                    List<Integer> positionIdx = PositionEnum.createIdxList(positions);
                    // GroupInfo 생성
                    GroupInfo groupInfo = GroupInfo.of(group, skillIdx, positionIdx);

                    // 작성자 정보 조회
                    UserInfo userInfo = userManager.findNodeUser(group.getUserId());

                    // 응답 객체 생성
                    return GroupInfoResponse.of(groupInfo, userInfo);
                })
                .toList();
    }


    public MyGroupSearchResponse searchMyGroups(MyGroupSearchCondition myCondition) {
        int baseSize = myCondition.getSize();
        List<Group> groups = groupQueryRepository.searchMyGroup(myCondition);
        log.debug("groups size : {}", groups.size());
        boolean hasNext = groups.size() > myCondition.getSize();

        if(hasNext){
            groups = groups.subList(0, baseSize);
        }
        List<GroupInfoResponse> result = groups.stream().map(group -> {
            // 기술 스택 조회
            List<String> skills = skillRepository.findByGroup(group).stream()
                    .map(Skill::getSkillInfo)
                    .toList();
            List<Integer> skillIdx = SkillEnum.createIdxList(skills);
            // 포지션 조회
            List<String> positions = positionRepository.findByGroup(group).stream()
                    .map(Position::getPositionInfo)
                    .toList();

            List<Integer> positionIdx = PositionEnum.createIdxList(positions);
            // GroupInfo 생성
            GroupInfo groupInfo = GroupInfo.of(group, skillIdx, positionIdx);

            // 작성자 정보 조회
            UserInfo userInfo = userManager.findNodeUser(group.getUserId());

            // 응답 객체 생성
            return GroupInfoResponse.of(groupInfo, userInfo);
        }).toList();

        Long nextCursor = result.isEmpty() ? null : groups.get(groups.size() - 1).getGroupId();
        return MyGroupSearchResponse.builder()
                .items(result)
                .hasNext(hasNext)
                .cursor(nextCursor)
                .build();
    }

    public List<UserInfo> findUserInfoOfGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(
                GroupException.GROUP_NOT_FOUND));

        List<Participant> users = participantRepository.findByGroup(group);

        return users.stream()
                .map(user -> {
                    long userId = user.getUserId();
                    UserInfo response = userManager.findNodeUser(userId);
                    return response;
                })
                .toList();
    }

    public List<UserInfo> findWaitingListOfGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(
                GroupException.GROUP_NOT_FOUND));

        List<WaitingList> users = waitingListRepository.findByGroup(group);

        return users.stream()
                .map(user -> {
                    long userId = user.getUserId();
                    UserInfo response = userManager.findNodeUser(userId);
                    return response;
                })
                .toList();
    }

    public void deleteGroupByGroupId(Long groupId) {
        Group group = groupRepository.findById(groupId).get();

        waitingListRepository.deleteByGroup(group);
        participantRepository.deleteByGroup(group);
        replyRepository.deleteByGroup(group);
        skillRepository.deleteByGroup(group);
        positionRepository.deleteByGroup(group);
        groupRepository.delete(group);

        log.debug("그룹 삭제가 완료되었습니다.");

    }


}
