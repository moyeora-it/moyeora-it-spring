package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;

import java.util.List;

public interface GroupQueryRepository {
    List<Group> searchGroup(GroupSearchCondition condition);
}
