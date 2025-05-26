package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.commons.enumdata.Position;
import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import com.moyeorait.moyeoraitspring.domain.user.repository.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"group\"") // 예약어 및 소문자 보존을 위해 따옴표 포함
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private LocalDateTime deadline;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    private String content;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    private Position positions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_user_id")
    private User firstUserId;

    private Integer views;

    @Enumerated(EnumType.STRING)
    private Skill skills;

    @Column(name = "type")
    private String type;

    private Boolean status;

    @Column(name = "auto_allow")
    private Boolean autoAllow;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
