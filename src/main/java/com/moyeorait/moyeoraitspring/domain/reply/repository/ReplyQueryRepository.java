package com.moyeorait.moyeoraitspring.domain.reply.repository;

import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReplySearchCondition;

import java.util.List;

public interface ReplyQueryRepository {
    List<Reply> searchByCondition(ReplySearchCondition condition, int pageSize);

    List<Reply> searchReReplyByCondition(ReReplySearchCondition condition, int i);
}
