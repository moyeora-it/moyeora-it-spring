package com.moyeorait.moyeoraitspring.domain.participant;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByGroupAndUserId(Group group, Long userId);

    int deleteByGroupAndUserId(Group saveGroup, Long userId);

    List<Participant> findByGroup(Group group);

    void deleteByGroup(Group group);
}
