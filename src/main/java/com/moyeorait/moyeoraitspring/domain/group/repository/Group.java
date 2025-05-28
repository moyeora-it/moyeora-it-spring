package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.commons.config.JpaConfig;
import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.commons.enumdata.Position;
import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"group\"") // 예약어 및 소문자 보존을 위해 따옴표 포함
@Getter
@Setter
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;

    @Column(name = "title", nullable = false)
    private String title;

    private String content;

    private Long userId;

    @Column(name = "auto_allow")
    private Boolean autoAllow;

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




}
