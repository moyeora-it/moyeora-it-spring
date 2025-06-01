package com.moyeorait.moyeoraitspring.domain.reply.repository.condition;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReReplySearchCondition {
    Reply reply;
    Long cursor;
    int size;

    public static ReReplySearchCondition of(ReplySearchRequest request, Reply reply) {
        return new ReReplySearchCondition(
                reply,
                request.getCursor(),
                request.getSize()
        );
    }
}
