package com.moyeorait.moyeoraitspring.domain.skill.repository;

import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Skill{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group ;

    private String skillInfo;

    public Skill(Group group, String skillName) {
        this.group = group;
        this.skillInfo = skillName;
    }
}
