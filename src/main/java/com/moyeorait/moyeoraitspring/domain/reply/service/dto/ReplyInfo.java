package com.moyeorait.moyeoraitspring.domain.reply.service.dto;

import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyInfo {

    private long replyId;
    private UserInfo userInfo;
    private String content;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplyInfo from(Reply reply, UserInfo userInfo){
        return new ReplyInfo(
                reply.getReplyId(),
                userInfo,
                reply.getContent(),
                reply.isDeleted(),
                reply.getCreatedAt(),
                reply.getUpdatedAt()
        );
    }
}
