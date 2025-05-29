package com.moyeorait.moyeoraitspring.domain.waitinglist.repository;

import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "waiting_list")
@NoArgsConstructor
public class WaitingList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private Long userId;

    public WaitingList(Group saveGroup, Long currentUserId) {
        this.group = saveGroup;
        this.userId = currentUserId;
    }
}
