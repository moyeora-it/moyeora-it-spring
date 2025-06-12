package com.moyeorait.moyeoraitspring.domain.group.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.MyGroupSearchCondition;

import java.util.List;

public interface GroupQueryRepository {
    List<Group> searchGroup(GroupSearchCondition condition);

    List<Group> searchMyGroup(MyGroupSearchCondition myCondition);

    List<Long> searchGroupIds(GroupSearchCondition condition);

    List<Group> searchGroupsByIds(List<Long> groupIds, String sort, String order);

    List<Long> searchMyGroupIds(MyGroupSearchCondition myCondition);

    List<Group> searchMyGroupsByIds(List<Long> groupIds, String sort, String order);
}
