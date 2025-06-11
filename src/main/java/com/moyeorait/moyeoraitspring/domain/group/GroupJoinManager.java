package com.moyeorait.moyeoraitspring.domain.group;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.test.TestException;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.JoinManageRequest;
import com.moyeorait.moyeoraitspring.domain.group.exception.GroupException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.participant.service.ParticipantService;
import com.moyeorait.moyeoraitspring.domain.user.UserService;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingList;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import com.moyeorait.moyeoraitspring.domain.waitinglist.service.WaitingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class GroupJoinManager {

    @Autowired
    GroupService groupService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    WaitingListService waitingListService;
    @Autowired
    private UserService userService;

    public void joinRequest(Long groupId, Long userId) {
        Group group = groupService.findById(groupId);

        isGroupFull(group);

        if(group.getAutoAllow()){
            participantService.addUserToGroup(group, userId);
        }
        else{
            waitingListService.addUserToGroup(group, userId);
        }
    }

    public void cancelRequest(Long groupId, Long userId) {
        Group group = groupService.findById(groupId);
        Participant participant = participantService.findByGroupAndUserId(group, userId);
        WaitingList waitingList = waitingListService.findByGroupAndUserId(group, userId);

        if(participant != null){
            // 참여 취소 및 현재 인원 감소
            participantService.deleteParticipant(participant);
            group.setCurrentParticipants(group.getCurrentParticipants() - 1);
        }else if(waitingList != null){
            waitingListService.deleteWaitingList(waitingList);
        }else{
            throw new CustomException(GroupException.PARTICIPATION_NOT_FOUND);
        }
    }
    private void isGroupFull(Group group) {
        if(group.getCurrentParticipants() >= group.getMaxParticipants()) {
            throw new CustomException(GroupException.GROUP_CAPACITY_EXCEEDED);
        }
    }


    public void manageJoinProcess(JoinManageRequest request, Long groupId, Long createUserId) {
        Group group = groupService.findById(groupId);
        if(!group.getUserId().equals(createUserId)) throw new CustomException(GroupException.USER_FORBIDDEN_ACCESS);

        if(request.getStatus().equals("approve")){
            if(group.getCurrentParticipants() >= group.getMaxParticipants())  throw new CustomException(GroupException.GROUP_CAPACITY_EXCEEDED);
            participantService.addUserToGroup(group, request.getUserId());
        }else if(request.getStatus().equals("deny")){
            participantService.denyToGroup(request.getUserId());
        }
        WaitingList waitingList = waitingListService.findByGroupAndUserId(group, request.getUserId());
        waitingListService.deleteWaitingList(waitingList);
    }
}
