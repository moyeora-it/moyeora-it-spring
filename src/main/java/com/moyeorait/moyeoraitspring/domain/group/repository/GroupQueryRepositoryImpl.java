package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.domain.bookmark.repository.QBookmark;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.MyGroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.participant.repository.QParticipant;
import com.moyeorait.moyeoraitspring.domain.position.repository.QPosition;
import com.moyeorait.moyeoraitspring.domain.skill.repository.QSkill;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.QWaitingList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.moyeorait.moyeoraitspring.domain.bookmark.repository.QBookmark.bookmark;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GroupQueryRepositoryImpl implements GroupQueryRepository{

    private final JPAQueryFactory queryFactory;
    QGroup group = QGroup.group;
    QSkill skill = QSkill.skill;
    QPosition position = QPosition.position;
    QWaitingList waitingList = QWaitingList.waitingList;
    QParticipant participant = QParticipant.participant;


    public List<Group> searchGroup(GroupSearchCondition condition){

        return queryFactory
                .select(group)
                .from(group)
                .leftJoin(skill).on(skill.group.eq(group))
                .leftJoin(position).on(position.group.eq(group))
                .where(
                        titleContains(condition.getKeyword()),
                        typeEq(condition.getType()),
                        skillIn(condition.getSkill()),
                        positionIn(condition.getPosition()),
                        cursorCondition(condition.getCursor(), condition.getOrder())
                )
                .orderBy(orderBy(condition.getSort(), condition.getOrder(), group))
                .limit(condition.getSize()+1)
                .fetch();
    }

    @Override
    public List<Group> searchMyGroup(MyGroupSearchCondition condition) {
        GroupSearchCondition base = condition.getCondition();
        Long userId = condition.getUserId();

        BooleanBuilder whereBuilder = new BooleanBuilder()
                .and(titleContains(base.getKeyword()))
                .and(typeEq(base.getType()))
                .and(skillIn(base.getSkill()))
                .and(positionIn(base.getPosition()))
                .and(cursorCondition(base.getCursor(), base.getOrder()));

        BooleanExpression joinFilter = null;

        if ("RECRUITING".equalsIgnoreCase(condition.getStatus())) {
            joinFilter = waitingList.userId.eq(userId);
        } else if ("PARTICIPANT".equalsIgnoreCase(condition.getStatus())) {
            joinFilter = participant.userId.eq(userId);
        } else {
            // 둘 다 포함
            joinFilter = waitingList.userId.eq(userId)
                    .or(participant.userId.eq(userId));
        }

        JPQLQuery<Group> query = queryFactory
                .select(group).distinct()
                .from(group)
                .leftJoin(skill).on(skill.group.eq(group))
                .leftJoin(position).on(position.group.eq(group))
                .leftJoin(waitingList).on(waitingList.group.eq(group))
                .leftJoin(participant).on(participant.group.eq(group))
                .where(whereBuilder.and(joinFilter))
                .orderBy(orderBy(base.getSort(), base.getOrder(), group))
                .limit(base.getSize() + 1);

        return query.fetch();
    }

    @Override
    public List<Long> searchGroupIds(GroupSearchCondition condition) {
        OrderSpecifier<?> orderSpecifier = orderBy(condition.getSort(), condition.getOrder(), group);
        List<String> requiredSkills = condition.getSkill();
        List<String> requiredPositions = condition.getPosition();

        BooleanBuilder whereBuilder = new BooleanBuilder()
                .and(titleContains(condition.getKeyword()))
                .and(typeEq(condition.getType()))
                .and(cursorCondition(condition.getCursor(), condition.getOrder()));

        if (requiredSkills != null && !requiredSkills.isEmpty()) {
            whereBuilder.and(skill.skillInfo.in(requiredSkills));
        }
        if (requiredPositions != null && !requiredPositions.isEmpty()) {
            whereBuilder.and(position.positionInfo.in(requiredPositions));
        }

        // ✅ 북마크 조건 처리
        if (condition.isBookmark()) {
            whereBuilder.and(bookmark.userId.eq(condition.getBookmarkUserId()));
        }

        BooleanExpression havingExpr = Expressions.TRUE;
        if (requiredSkills != null && !requiredSkills.isEmpty()) {
            havingExpr = havingExpr.and(skill.skillInfo.countDistinct().eq((long) requiredSkills.size()));
        }
        if (requiredPositions != null && !requiredPositions.isEmpty()) {
            havingExpr = havingExpr.and(position.positionInfo.countDistinct().eq((long) requiredPositions.size()));
        }

        JPQLQuery<Long> query = queryFactory
                .select(group.groupId)
                .from(group)
                .join(skill).on(skill.group.eq(group))
                .join(position).on(position.group.eq(group))
                .leftJoin(bookmark).on(bookmark.group.eq(group))
                .where(whereBuilder)
                .groupBy(group.groupId)
                .having(havingExpr)
                .orderBy(orderSpecifier)
                .limit(condition.getSize() + 1);

        return query.fetch();
    }

    private Path<?> getSortPath(String sort, QGroup group) {
        if ("start_date".equalsIgnoreCase(sort)) {
            return group.startDate;
        } else if ("end_date".equalsIgnoreCase(sort)) {
            return group.endDate;
        } else if ("deadline".equalsIgnoreCase(sort)) {
            return group.deadline;
        } else {
            return group.createdAt; // 기본 정렬 필드
        }
    }

    @Override
    public List<Group> searchGroupsByIds(List<Long> ids, String sort, String order) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        return queryFactory
                .select(group)
                .from(group)
                .where(group.groupId.in(ids))
                .orderBy(orderBy(sort, order, group))
                .fetch();
    }

    @Override
    public List<Long> searchMyGroupIds(MyGroupSearchCondition myCondition) {
        log.debug("내 그룹 조회 쿼리");
        GroupSearchCondition condition = myCondition.getCondition();
        Long userId = myCondition.getUserId();
        String status = myCondition.getStatus();

        OrderSpecifier<?> orderSpecifier = orderBy(condition.getSort(), condition.getOrder(), group);

        List<String> requiredSkills = condition.getSkill();
        List<String> requiredPositions = condition.getPosition();

        BooleanBuilder whereBuilder = new BooleanBuilder()
                .and(titleContains(condition.getKeyword()))
                .and(typeEq(condition.getType()))
                .and(cursorCondition(condition.getCursor(), condition.getOrder()));

        BooleanExpression joinFilter;
        log.debug("sataus : {}", status);
        if ("RECRUITING".equalsIgnoreCase(status)) {
            joinFilter = waitingList.userId.eq(userId);
        } else if ("PARTICIPANT".equalsIgnoreCase(status)) {
            joinFilter = participant.userId.eq(userId);
        } else if ("ENDED".equalsIgnoreCase(status)) {
            joinFilter = group.endDate.loe(LocalDateTime.now()); // 오늘 이전이면 종료된 것
        } else {
            // status가 null이거나 다른 값이면 둘 다 포함
            joinFilter = waitingList.userId.eq(userId).or(participant.userId.eq(userId));
        }

        BooleanExpression havingExpr = Expressions.TRUE;
        if (requiredSkills != null && !requiredSkills.isEmpty()) {
            havingExpr = havingExpr.and(skill.skillInfo.countDistinct().eq((long) requiredSkills.size()));
            whereBuilder.and(skill.skillInfo.in(requiredSkills));
        }
        if (requiredPositions != null && !requiredPositions.isEmpty()) {
            havingExpr = havingExpr.and(position.positionInfo.countDistinct().eq((long) requiredPositions.size()));
            whereBuilder.and(position.positionInfo.in(requiredPositions));
        }

        List<Long> groupIds = queryFactory
                .select(group.groupId)
                .from(group)
                .leftJoin(skill).on(skill.group.eq(group))
                .leftJoin(position).on(position.group.eq(group))
                .leftJoin(waitingList).on(waitingList.group.eq(group))
                .leftJoin(participant).on(participant.group.eq(group))
                .where(whereBuilder.and(joinFilter))
                .groupBy(group.groupId)
                .having(havingExpr)
                .orderBy(orderSpecifier)
                .limit(condition.getSize() + 1)
                .fetch();

        return groupIds;
    }

    @Override
    public List<Group> searchMyGroupsByIds(List<Long> groupIds, String sort, String order) {
        return List.of();
    }


    private BooleanExpression cursorCondition(Long cursor, String order) {
        if (cursor == null) return null;
        if ("desc".equalsIgnoreCase(order)) {
            return group.groupId.lt(cursor);
        }
        // 기본은 DESC로 간주
        return group.groupId.gt(cursor);
    }

    private BooleanExpression titleContains(String search) {
        return StringUtils.hasText(search) ? QGroup.group.title.containsIgnoreCase(search) : null;
    }

    private BooleanExpression typeEq(String type) {
        return StringUtils.hasText(type) ? group.type.eq(type) : null;
    }

    private BooleanExpression skillIn(List<String> skills) {
        return skills != null && !skills.isEmpty() ? QSkill.skill.skillInfo.in(skills) : null;
    }

    private BooleanExpression positionIn(List<String> positions) {
        return positions != null && !positions.isEmpty() ? QPosition.position.positionInfo.in(positions) : null;
    }
    private BooleanExpression cursorLt(Long cursor) {
        if (cursor == null) return null;
        return QGroup.group.groupId.lt(cursor);
    }
    private OrderSpecifier<?> orderBy(String sort, String order) {
        boolean asc = "asc".equalsIgnoreCase(order);
        if ("deadline".equalsIgnoreCase(sort)) {
            return asc ? QGroup.group.deadline.asc() : QGroup.group.deadline.desc();
        } else {
            return asc ? QGroup.group.createdAt.asc() : QGroup.group.createdAt.desc();
        }
    }

    private OrderSpecifier<?> orderBy(String sort, String order, QGroup group) {
        PathBuilder<Group> path = new PathBuilder<>(Group.class, "group");

        if ("createdAt".equalsIgnoreCase(sort)) {
            return "desc".equalsIgnoreCase(order) ? group.createdAt.desc() : group.createdAt.asc();
        }
        // 기본은 groupId
        return "desc".equalsIgnoreCase(order) ? group.groupId.desc() : group.groupId.asc();
    }
}
