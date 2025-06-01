package com.moyeorait.moyeoraitspring.domain.reply.repository;

import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReplySearchCondition;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReplyQueryRepositoryImpl implements ReplyQueryRepository{

    @Autowired
    JPAQueryFactory queryFactory;
    @Override
    public List<Reply> searchByCondition(ReplySearchCondition condition, int pageSize) {
        QReply reply = QReply.reply;

        return queryFactory
                .select(reply)
                .from(reply)
                .where(
                        reply.group.eq(condition.getGroup()), // 그룹 게시글 중
                        reply.parent.isNull(), // 대댓글빼고, 댓글만
                        cursorGt(condition.getCursor()) // 이전 댓글만
                )
                .orderBy(reply.replyId.asc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<Reply> searchReReplyByCondition(ReReplySearchCondition condition, int pageSize) {
        QReply reply = QReply.reply;

        return queryFactory
                .select(reply)
                .from(reply)
                .where(
                        reply.parent.eq(condition.getReply()),
                        cursorGt(condition.getCursor())
                )
                .orderBy(reply.replyId.asc())
                .limit(pageSize)
                .fetch();
    }

    private Predicate cursorLt(Long cursor) {
        if (cursor == null) return null;
        return QReply.reply.replyId.lt(cursor);
    }
    private Predicate cursorGt(Long cursor) {
        if (cursor == null) return null;
        return QReply.reply.replyId.gt(cursor);
    }

}
