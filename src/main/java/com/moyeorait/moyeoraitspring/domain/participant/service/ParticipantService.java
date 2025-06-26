package com.moyeorait.moyeoraitspring.domain.participant.service;

import com.moyeorait.moyeoraitspring.commons.enumdata.NotificationType;
import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.domain.group.exception.GroupException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.user.notification.NotificationManager;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantService {
    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    NotificationManager notificationManager;

    @Autowired
    WaitingListRepository waitingListRepository;

    public void addUserToGroup(Group group, Long participantUserId) {
        if(participantRepository.findByGroupAndUserId(group, participantUserId) != null) {
            throw new CustomException(GroupException.AREADY_REQUEST_USER);
        }

        group.incrementParticipants();
//        try {
//            Thread.sleep(100); // race condition 유도
//        } catch (InterruptedException e) {
//            System.out.println("에러발생");
//            throw new RuntimeException(e);
//        }

        Participant participant = new Participant(group, participantUserId);
        participantRepository.save(participant);

        sendCreateGroupNotification(group, participantUserId);
    }

    private void sendCreateGroupNotification(Group group, Long participantUserId) {

        Long groupId = group.getGroupId();
        String groupUrl = "/groups/" + groupId;
        String participantUrl = groupUrl + "/participants";

        if (group.getCurrentParticipants() >= group.getMaxParticipants()) {
            notificationManager.sendNotification(NotificationType.FULL_CAPACITY, group.getUserId(), groupUrl);
        }

        notificationManager.sendNotification(NotificationType.APPLY_APPROVED, participantUserId, groupUrl);
        notificationManager.sendNotification(NotificationType.GROUP_HAS_PARTICIPANT, group.getUserId(), participantUrl);

    }


    public Participant findByGroupAndUserId(Group group, Long userId) {
        Participant participant = participantRepository.findByGroupAndUserId(group, userId);
        return participant;
    }

    public void deleteParticipant(Participant participant) {
        participantRepository.delete(participant);

        Long groupId = participant.getGroup().getGroupId();
        Long userId = participant.getUserId();
        String url = String.format("/groups/%d", groupId);
        notificationManager.sendNotification(NotificationType.CONFIRMED_PARTICIPANT_CANCELED, userId, url);
    }

    public void denyToGroup(Long requestUserId) {
        notificationManager.sendNotification(NotificationType.APPLY_REJECTED, requestUserId, "");
    }
}
