package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"group\"") // 예약어 및 소문자 보존을 위해 따옴표 포함
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "title", nullable = false)
    private String title;

    private String content;

    private Long userId;

    @Column(name = "auto_allow")
    private Boolean autoAllow;

    @Column(name = "current_participants")
    private Integer currentParticipants;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    private Boolean status;


    @Column(name = "type")
    private String type;

    private Integer views;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "start_date")
    private LocalDateTime startDate;


    @Column(name = "end_date")
    private LocalDateTime endDate;


    public Group(CreateGroupRequest request, Long userId) {
        this.title = request.getTitle();
        this.content = request.getDescription();
        this.userId = userId;
        this.autoAllow = request.isAutoAllow();
        this.currentParticipants = 1;
        this.maxParticipants = request.getMaxParticipants();
        this.status = true;
        this.type = request.getType();
        this.views = 0;
        this.deadline = request.getDeadline();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

    public void incrementParticipants() {
        this.currentParticipants += 1;
        System.out.println("참여인원 로그 : " + this.currentParticipants);
    }
}
