package com.moyeorait.moyeoraitspring.domain.group.controller;

import com.moyeorait.moyeoraitspring.commons.annotation.Login;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.group.GroupJoinManager;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.JoinManageRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.Content;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.response.ReplySearchResponse;
import com.moyeorait.moyeoraitspring.domain.reply.service.ReplyService;
import com.moyeorait.moyeoraitspring.domain.user.UserInfo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/groups")
@Slf4j
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupJoinManager groupJoinManager;

    @Autowired
    ReplyService replyService;


    @PostMapping
    public ApiResponse<Void> requestCreateGroup(@Login Long userId, @Valid @RequestBody CreateGroupRequest request){

        groupService.createGroup(request, userId);

        System.out.println("test");
        log.debug("request : {}", request);

        return ApiResponse.success();
    }

    @GetMapping("/{groupId}")
    public ApiResponse<GroupInfoResponse> findGroup(@PathVariable Long groupId){
        log.debug("findGroupId : {}", groupId);
        GroupInfoResponse result = groupService.findGroupByGroupId(groupId);

        return ApiResponse.success(result);
    }

    @GetMapping
    public ApiResponse<List<GroupInfoResponse>> findGroups(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) List<String> skill,
            @RequestParam(required = false) List<String> position,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
            ){

        GroupSearchCondition condition = GroupSearchCondition.builder()
                .sort(sort)
                .order(order)
                .skill(skill)
                .position(position)
                .type(type)
                .keyword(keyword).build();
        List<GroupInfoResponse> result = groupService.searchGroups(condition);
        return ApiResponse.success(result);
    }

    @PostMapping("/{groupId}/applications")
    public ApiResponse<Void> joinRequestGroup(@Login Long userId, @PathVariable Long groupId){

        groupJoinManager.joinRequest(groupId, userId);

        return ApiResponse.success();
    }

    @DeleteMapping("/{groupId}/applications")
    public ApiResponse<Void> leaveRequestGroup(@Login Long userId, @PathVariable Long groupId){
        groupJoinManager.cancelRequest(groupId, userId);

        return ApiResponse.success();
    }

    @PostMapping("/{groupId}/join")
    public ApiResponse<Void> manageJoinRequest(@Login Long userId, @PathVariable Long groupId, @RequestBody JoinManageRequest request){

        groupJoinManager.manageJoinProcess(request, groupId, userId);

        return ApiResponse.success();
    }

    @GetMapping("/{groupId}/replies")
    public ApiResponse<ReplySearchResponse> searchReplyRequest(@RequestBody ReplySearchRequest request, @PathVariable Long groupId){
        ReplySearchResponse result = replyService.searchReply(request, groupId);
        return ApiResponse.success(result);
    }


    @PostMapping("/{groupId}/replies")
    public ApiResponse<Void> createReplyRequest(@Login Long userId,@RequestBody Content content, @PathVariable Long groupId){

        ReplySaveRequest request = new ReplySaveRequest(groupId, userId, null, content.getContent());
        replyService.saveReply(request);

        return ApiResponse.success();
    }

    @GetMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<ReplySearchResponse> searchReReplyReauest(@RequestBody ReplySearchRequest request, @PathVariable Long replyId){
        ReplySearchResponse result = replyService.searchReReply(request, replyId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<Void> createChildReplyRequest(@Login Long userId,@PathVariable Long groupId, @PathVariable Long replyId, @RequestBody Content content){
        ReplySaveRequest request = new ReplySaveRequest(groupId, userId, replyId, content.getContent());
        replyService.saveReply(request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<Void> deleteReplyRequest(@Login Long userId, @PathVariable Long groupId, @PathVariable Long replyId){

        replyService.deleteByReplyId(replyId);

        return ApiResponse.success();
    }


    @GetMapping("/{groupId}/participants")
    public ApiResponse<List<UserInfo>> findUserInfoOfGroup(@PathVariable Long groupId){
        List<UserInfo> result = groupService.findUserInfoOfGroup(groupId);
        return ApiResponse.success(result);
    }
}
