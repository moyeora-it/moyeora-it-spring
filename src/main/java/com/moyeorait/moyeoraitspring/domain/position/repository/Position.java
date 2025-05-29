package com.moyeorait.moyeoraitspring.domain.position.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    private String positionInfo;

    public Position(Group saveGroup, String positionInfo) {
        this.group = saveGroup;
        this.positionInfo = positionInfo;
    }
}
