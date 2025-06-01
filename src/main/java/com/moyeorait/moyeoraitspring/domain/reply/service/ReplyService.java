package com.moyeorait.moyeoraitspring.domain.reply.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.reply.controller.response.ReplySearchResponse;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.reply.service.dto.ReplyInfo;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyQueryRepository;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReplyService {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ReplyQueryRepository replyQueryRepository;

    @Autowired
    UserManager userManager;

    public void saveReply(ReplySaveRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).get();
        Reply parentReply = request.getParentId() == null? null: replyRepository.findById(request.getParentId()).get();
        Reply reply = Reply.of(request, group, parentReply);

        replyRepository.save(reply);
    }

    public void deleteByReplyId(Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        reply.setDeleted(true);
    }

    public ReplySearchResponse searchReply(ReplySearchRequest request, Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        ReplySearchCondition condition = ReplySearchCondition.of(request, group);

        int pageSize = request.getSize();
        List<Reply> replies =  replyQueryRepository.searchByCondition(condition, pageSize + 1);

        boolean hasNext = replies.size() > pageSize;
        if(hasNext) replies = replies.subList(0, pageSize);

        List<ReplyInfo> items = replies.stream()
                .map(reply -> {
                    Long userId = reply.getUserId();
                    UserInfo userInfo = userManager.findNodeUser(userId);

                    return ReplyInfo.from(reply, userInfo);
                })
                .toList();

        Long cursor = replies.isEmpty() ? null : replies.get(replies.size() - 1).getReplyId();
        ReplySearchResponse response = new ReplySearchResponse(items, hasNext, cursor);


        return response;
    }

    public ReplySearchResponse searchReReply(ReplySearchRequest request, Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        ReReplySearchCondition condition = ReReplySearchCondition.of(request, reply);
        int pageSize = request.getSize();
        List<Reply> replies = replyQueryRepository.searchReReplyByCondition(condition, pageSize + 1);

        boolean hasNext = replies.size() > pageSize;
        if(hasNext) replies = replies.subList(0, pageSize);
        List<ReplyInfo> items = replies.stream()
                .map(replyItem -> {
                    Long userId = replyItem.getUserId();
                    UserInfo userInfo = userManager.findNodeUser(userId);

                    return ReplyInfo.from(replyItem, userInfo);
                })
                .toList();

        Long cursor = replies.isEmpty() ? null : replies.get(replies.size() - 1).getReplyId();
        ReplySearchResponse response = new ReplySearchResponse(items, hasNext, cursor);

        return response;

    }
}
