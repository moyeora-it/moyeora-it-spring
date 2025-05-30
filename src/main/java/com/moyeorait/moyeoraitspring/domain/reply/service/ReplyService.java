package com.moyeorait.moyeoraitspring.domain.reply.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    GroupRepository groupRepository;

    public void saveReply(ReplySaveRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).get();
        Reply parentReply = request.getParentId() == null? null: replyRepository.findById(request.getParentId()).get();
        Reply reply = Reply.of(request, group, parentReply);

        replyRepository.save(reply);
    }
}
