package com.moyeorait.moyeoraitspring.domain.user.repository;

import com.moyeorait.moyeoraitspring.commons.config.JpaConfig;
import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.commons.enumdata.Position;
import com.moyeorait.moyeoraitspring.commons.enumdata.Provider;
import com.moyeorait.moyeoraitspring.commons.enumdata.Skill;
import com.moyeorait.moyeoraitspring.domain.group.repository.converter.SkillListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Imported;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"user\"") // 예약어 보호용
@Getter
@Setter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @Column(name = "password")
    private String password;

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Position position;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills", columnDefinition = "text[]")
    private List<String> skills;


    @Column(name = "email_authentication")
    private Integer emailAuthentication;


}