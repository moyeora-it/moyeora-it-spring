package com.moyeorait.moyeoraitspring.domain.participant.repository;

import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Participant extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private Long userId;

    public Participant(Group saveGroup, Long userId) {
        this.group = saveGroup;
        this.userId = userId;
    }
}
