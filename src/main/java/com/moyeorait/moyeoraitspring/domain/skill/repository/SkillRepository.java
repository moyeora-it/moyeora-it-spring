package com.moyeorait.moyeoraitspring.domain.skill.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByGroup(Group group);

    void deleteByGroup(Group group);
}
