package com.moyeorait.moyeoraitspring.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
}
