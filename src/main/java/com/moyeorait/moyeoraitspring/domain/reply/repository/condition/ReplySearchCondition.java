package com.moyeorait.moyeoraitspring.domain.reply.repository.condition;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReplySearchCondition {
    Group group;
    Long cursor;
    int size;

    public static ReplySearchCondition of(ReplySearchRequest request, Group group) {
        return new ReplySearchCondition(
                group,
                request.getCursor(),
                request.getSize()
        );
    }
}
