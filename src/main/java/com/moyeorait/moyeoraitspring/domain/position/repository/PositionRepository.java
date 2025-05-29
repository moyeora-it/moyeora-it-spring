package com.moyeorait.moyeoraitspring.domain.position.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByGroup(Group group);
}
