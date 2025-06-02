package com.moyeorait.moyeoraitspring.domain.reply.repository;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.reply.service.ReplyService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    void deleteByGroup(Group group);
}
