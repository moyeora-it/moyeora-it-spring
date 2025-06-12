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
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
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

        // 정렬 기준에 따라 함께 SELECT
        Path<?> sortPath = getSortPath(condition.getSort(), group);

        List<Tuple> tuples = queryFactory
                .select(group.groupId, sortPath)
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
                .distinct()
                .orderBy(orderSpecifier)
                .limit(condition.getSize() + 1)
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(group.groupId))
                .toList();
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
        GroupSearchCondition condition = myCondition.getCondition();
        Long userId = myCondition.getUserId();
        String status = myCondition.getStatus();

        OrderSpecifier<?> orderSpecifier = orderBy(condition.getSort(), condition.getOrder(), group);
        Path<?> sortPath = getSortPath(condition.getSort(), group);

        BooleanBuilder whereBuilder = new BooleanBuilder()
                .and(titleContains(condition.getKeyword()))
                .and(typeEq(condition.getType()))
                .and(skillIn(condition.getSkill()))
                .and(positionIn(condition.getPosition()))
                .and(cursorCondition(condition.getCursor(), condition.getOrder()));

        BooleanExpression joinFilter;
        if ("RECRUITING".equalsIgnoreCase(status)) {
            joinFilter = waitingList.userId.eq(userId);
        } else if ("PARTICIPANT".equalsIgnoreCase(status)) {
            joinFilter = participant.userId.eq(userId);
        } else {
            // status가 null이거나 다른 값이면 둘 다 포함
            joinFilter = waitingList.userId.eq(userId).or(participant.userId.eq(userId));
        }
        List<Tuple> tuples = queryFactory
                .select(group.groupId, sortPath)
                .from(group)
                .leftJoin(skill).on(skill.group.eq(group))
                .leftJoin(position).on(position.group.eq(group))
                .leftJoin(waitingList).on(waitingList.group.eq(group))
                .leftJoin(participant).on(participant.group.eq(group))
                .where(whereBuilder.and(joinFilter))
                .distinct()
                .orderBy(orderSpecifier)
                .limit(condition.getSize() + 1)
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(group.groupId))
                .toList();
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
