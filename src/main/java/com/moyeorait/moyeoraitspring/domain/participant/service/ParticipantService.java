package com.moyeorait.moyeoraitspring.domain.participant.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantService {
    @Autowired
    ParticipantRepository participantRepository;

    public void addUserToGroup(Group group, Long userId) {
        Participant participant = new Participant(group, userId);
        participantRepository.save(participant);


    }
}
