package com.moyeorait.moyeoraitspring.domain.reply.service;

import com.moyeorait.moyeoraitspring.commons.enumdata.NotificationType;
import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.domain.group.exception.GroupException;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplyUpdateRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.response.ReplySearchResponse;
import com.moyeorait.moyeoraitspring.domain.reply.exception.ReplyException;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.reply.service.dto.ReplyInfo;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import com.moyeorait.moyeoraitspring.domain.reply.repository.Reply;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyQueryRepository;
import com.moyeorait.moyeoraitspring.domain.reply.repository.ReplyRepository;
import com.moyeorait.moyeoraitspring.domain.reply.repository.condition.ReplySearchCondition;
import com.moyeorait.moyeoraitspring.domain.user.UserException;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import com.moyeorait.moyeoraitspring.domain.user.notification.NotificationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ReplyService {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ReplyQueryRepository replyQueryRepository;

    @Autowired
    UserManager userManager;


    @Autowired
    NotificationManager notificationManager;

    public Long saveReply(ReplySaveRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).get();
        Reply parentReply = request.getParentId() == null? null: replyRepository.findById(request.getParentId()).get();
        Reply reply = Reply.of(request, group, parentReply);

        replyRepository.save(reply);

        Long groupId = group.getGroupId();
        String url = String.format("/group/%d", groupId);
        notificationManager.sendNotification(NotificationType.COMMENT_RECEIVED, group.getUserId(), url);

        return reply.getReplyId();
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
        log.debug("replies size : {}", replies.size());
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
        log.debug("replies size : {}", replies.size());

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

    public void updateReply(ReplyUpdateRequest request) {

        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(() -> new CustomException(GroupException.GROUP_NOT_FOUND));
        Reply reply = replyRepository.findById(request.getReplyId()).orElseThrow(() -> new CustomException(ReplyException.REPLY_NOT_FOUND));
        if(reply.getUserId() != request.getUserId()) throw new CustomException(GroupException.USER_FORBIDDEN_ACCESS);
        reply.setContent(request.getContent());

    }
}
