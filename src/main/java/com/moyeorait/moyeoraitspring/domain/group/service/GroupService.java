package com.moyeorait.moyeoraitspring.domain.group.service;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.position.repository.Position;
import com.moyeorait.moyeoraitspring.domain.position.repository.PositionRepository;
import com.moyeorait.moyeoraitspring.domain.skill.repository.Skill;
import com.moyeorait.moyeoraitspring.domain.skill.repository.SkillRepository;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserNodeResponse;
import com.moyeorait.moyeoraitspring.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userservice;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    public Group createGroup(CreateGroupRequest request, Long userId) {
        Group group = new Group(request, userId);
        Group result = groupRepository.save(group);

        List<Skill> skillEntities = request.getSkills().stream()
                .map(skillInfo -> new Skill(result, skillInfo))
                .toList();

        skillRepository.saveAll(skillEntities);

        List<Position> positionEntities =request.getPosition().stream()
                .map(positionInfo -> new Position(result, positionInfo))
                .toList();

        positionRepository.saveAll(positionEntities);

        return result;
    }

    public GroupInfoResponse findGroupByGroupId(Long groupId) {

        Group group = groupRepository.findById(groupId).get();

        List<String> skills = skillRepository.findByGroup(group).stream()
                .map(Skill::getSkillInfo)
                .toList();

        List<String> positions = positionRepository.findByGroup(group).stream()
                .map(Position::getPositionInfo)
                .toList();

        GroupInfo groupInfo = GroupInfo.of(group, skills, positions);

        Long userId = group.getUserId();
        UserNodeResponse response = userservice.getUserInfo(userId);
        UserInfo userInfo = UserInfo.from(response);

        GroupInfoResponse result = GroupInfoResponse.of(groupInfo, userInfo);

        return result;
    }
}
