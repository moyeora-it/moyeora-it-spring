package com.moyeorait.moyeoraitspring.domain.group;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.test.TestException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.service.ParticipantService;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import com.moyeorait.moyeoraitspring.domain.waitinglist.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GroupJoinManager {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ParticipantService participantService;

    @Autowired
    WaitingListService waitingListService;

    public void joinRequest(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).get();

        if(group.getAutoAllow()){
            participantService.addUserToGroup(group, userId);
        }
        else{
            waitingListService.addUserToGroup(group, userId);
        }
    }


}
