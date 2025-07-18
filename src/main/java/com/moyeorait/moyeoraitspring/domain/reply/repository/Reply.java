package com.moyeorait.moyeoraitspring.domain.reply.repository;

import com.moyeorait.moyeoraitspring.commons.entity.BaseTimeEntity;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    private String content;

    private boolean isDeleted;


    public static Reply of(ReplySaveRequest request, Group group, Reply parentReply) {
        return Reply.builder()
                .group(group)
                .userId(request.getUserId())
                .parent(parentReply)
                .content(request.getContent())
                .isDeleted(false)
                .build();
    }
}
