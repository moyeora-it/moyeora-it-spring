package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.domain.bookmark.repository.QBookmark;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.position.repository.QPosition;
import com.moyeorait.moyeoraitspring.domain.skill.repository.QSkill;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepositoryImpl implements GroupQueryRepository{

    private final JPAQueryFactory queryFactory;
    QGroup group = QGroup.group;
    QSkill skill = QSkill.skill;
    QPosition position = QPosition.position;

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
                        positionIn(condition.getPosition())
                )
                .distinct()
                .orderBy(orderBy(condition.getSort(), condition.getOrder()))
                .fetch();
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

    private OrderSpecifier<?> orderBy(String sort, String order) {
        boolean asc = "asc".equalsIgnoreCase(order);
        if ("deadline".equalsIgnoreCase(sort)) {
            return asc ? QGroup.group.deadline.asc() : QGroup.group.deadline.desc();
        } else {
            return asc ? QGroup.group.createdAt.asc() : QGroup.group.createdAt.desc();
        }
    }
}
