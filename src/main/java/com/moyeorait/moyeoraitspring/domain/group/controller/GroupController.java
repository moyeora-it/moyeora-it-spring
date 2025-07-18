package com.moyeorait.moyeoraitspring.domain.group.controller;

import com.moyeorait.moyeoraitspring.commons.annotation.Login;
import com.moyeorait.moyeoraitspring.commons.enumdata.PositionEnum;
import com.moyeorait.moyeoraitspring.commons.enumdata.SkillEnum;
import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.response.ApiPageResponse;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.group.GroupJoinManager;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.GroupSearchRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.JoinManageRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoJoinResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupPagingResponse;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.MyGroupSearchResponse;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.GroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.repository.condition.MyGroupSearchCondition;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.group.service.info.GroupInfo;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.Content;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySaveRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplySearchRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.request.ReplyUpdateRequest;
import com.moyeorait.moyeoraitspring.domain.reply.controller.response.ReplySearchResponse;
import com.moyeorait.moyeoraitspring.domain.reply.service.ReplyService;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfoList;
import com.moyeorait.moyeoraitspring.domain.user.exception.UserException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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


    @Operation(summary = "그룹 생성", description = "로그인 필요, 요청 정보를 기반으로 그룹을 생성합니다.")
    @PostMapping
    public ApiResponse<Long> requestCreateGroup(@Login Long userId, @Valid @RequestBody CreateGroupRequest request){
        log.debug("createGroup userId : ", userId);
        Long groupId = groupService.createGroup(request, userId);

        log.debug("request : {}", request);

        return ApiResponse.success(groupId);
    }


    @Operation(summary = "그룹 조건 조회", description = "조건을 기반으로 그룹을 조회합니다.")
    @GetMapping
    public ApiPageResponse findGroups(
            @ModelAttribute @Validated GroupSearchRequest request,
            @Login(required = false) Long userId
    ){
        log.debug(request.toString());
        List<String> skillList = SkillEnum.createStringList(request.getSkill());
        List<String> positionList = PositionEnum.createStringList(request.getPosition());
        GroupSearchCondition condition = GroupSearchCondition.builder()
                .sort(request.getSort())
                .order(request.getOrder())
                .skill(skillList)
                .position(positionList)
                .type(request.getType())
                .size(request.getSize())
                .cursor(request.getCursor())
                .keyword(request.getSearch())
                .build();

        GroupPagingResponse result = groupService.searchGroups(condition, userId);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
    }

    @Operation(summary = "북마크 그룹 조건 조회", description = "조건을 기반으로 즐겨찾기 한 그룹을 조회합니다.")
    @GetMapping("/bookmark")
    public ApiPageResponse findBookmarkGroups(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) List<Integer> skill,
            @RequestParam(required = false) List<Integer> position,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam Integer size,
            @RequestParam(required = false) Long cursor,
            @Login(required = false) Long userId
    ){
        if(userId == null) throw new CustomException(UserException.USER_INFO_NOT_FOUND);
        List<String> skillList = SkillEnum.createStringList(skill);
        List<String> positionList = PositionEnum.createStringList(position);
        GroupSearchCondition condition = GroupSearchCondition.builder()
                .sort(sort)
                .order(order)
                .skill(skillList)
                .position(positionList)
                .type(type)
                .size(size)
                .cursor(cursor)
                .keyword(search)
                .bookmark(true)
                .bookmarkUserId(userId).build();
        log.debug("size : {}", size);
        GroupPagingResponse result = groupService.searchGroups(condition, userId);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
    }


    @Operation(summary = "그룹 상세 정보 조회", description = "그룹의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ApiResponse<GroupInfoJoinResponse> findGroup(@PathVariable Long groupId, @Login(required = false) Long userId){
        log.debug("<<< 그룹 상세 조회 api요청 >>>");
        log.debug("userId: {}", userId);
        GroupInfoJoinResponse result = groupService.findGroupByGroupId(groupId, userId);

        return ApiResponse.success(result);
    }

    @Operation(summary = "그룹 모임 취소", description = "주최자가 모임을 취소합니다.")
    @DeleteMapping("/{groupId}")
    public ApiResponse<Long> deleteGroup(@PathVariable Long groupId, @Login Long userId){
        log.debug("deleteGroup : {}", groupId);

        groupService.deleteGroupByGroupId(groupId);

        return ApiResponse.success();
    }


    @Operation(summary = "마이페이지 그룹조회", description = "자신이 참여하고 있는 그룹에 대해 조건을 기반으로 조회합니다.")
    @GetMapping("/mygroup")
    public ApiPageResponse findMyGroups(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) List<Integer> skill,
            @RequestParam(required = false) List<Integer> position,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam Integer size,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) String search,
            @Login Long userId
    ){
        List<String> skillList = SkillEnum.createStringList(skill);
        List<String> positionList = PositionEnum.createStringList(position);
        GroupSearchCondition condition = GroupSearchCondition.builder()
                .sort(sort)
                .order(order)
                .skill(skillList)
                .position(positionList)
                .type(type)
                .size(size) // hasNext판별을 위해 미리 +1
                .cursor(cursor)
                .keyword(search).build();
        MyGroupSearchCondition myCondition = MyGroupSearchCondition.builder()
                .condition(condition)
                .status(status)
                .userId(userId)
                .build();
        MyGroupSearchResponse result = groupService.searchMyGroups(myCondition);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
    }

    @Operation(summary = "마이페이지 그룹조회", description = "자신이 참여하고 있는 그룹에 대해 조건을 기반으로 조회합니다.")
    @GetMapping("/usergroup/{userId}")
    public ApiPageResponse findUserGroups(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) List<Integer> skill,
            @RequestParam(required = false) List<Integer> position,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam Integer size,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) String search,
            @PathVariable Long userId
    ){
        List<String> skillList = SkillEnum.createStringList(skill);
        List<String> positionList = PositionEnum.createStringList(position);
        GroupSearchCondition condition = GroupSearchCondition.builder()
                .sort(sort)
                .order(order)
                .skill(skillList)
                .position(positionList)
                .type(type)
                .size(size) // hasNext판별을 위해 미리 +1
                .cursor(cursor)
                .keyword(search).build();
        MyGroupSearchCondition myCondition = MyGroupSearchCondition.builder()
                .condition(condition)
                .status(status)
                .userId(userId)
                .build();
        MyGroupSearchResponse result = groupService.searchMyGroups(myCondition);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
    }


    @Operation(summary = "추천 그룹 조회", description = "유저 정보를 기반으로 추천된 그룹 리스트를 반환합니다.")
    @GetMapping("/recommend")
    public ApiResponse<List<GroupInfo>> recommendGroups(){
        GroupPagingResponse groupResponse = groupService.searchGroups(GroupSearchCondition.builder()
                .cursor(0L)
                .size(100000)
                .build(), null);

        List<GroupInfo> result = groupResponse.getItems().stream()
                .filter(group -> group.getDeadline() != null && group.getDeadline().isAfter(LocalDateTime.now()))
                .toList();

        return ApiResponse.success(result);
    }


    @Operation(summary = "그룹 참여 요청", description = "로그인 필요, 그룹참여를 요청합니다.")
    @PostMapping("/{groupId}/applications")
    public ApiResponse<Void> joinRequestGroup(@Login Long userId, @PathVariable Long groupId){

        groupJoinManager.joinRequest(groupId, userId);

        return ApiResponse.success();
    }

    @Operation(summary = "그룹 참여 취소", description = "로그인 필요, 그룹 참여를 취소합니다.")
    @DeleteMapping("/{groupId}/applications")
    public ApiResponse<Void> leaveRequestGroup(@Login Long userId, @PathVariable Long groupId){
        groupJoinManager.cancelRequest(groupId, userId);

        return ApiResponse.success();
    }


    @Operation(summary = "그룹 참여 관리", description = "로그인 필요, 대기 상태인 그룹참여를 수락하거나, 거절합니다.")
    @PostMapping("/{groupId}/join")
    public ApiResponse<Void> manageJoinRequest(@Login Long userId, @PathVariable Long groupId, @RequestBody JoinManageRequest request){

        groupJoinManager.manageJoinProcess(request, groupId, userId);

        return ApiResponse.success();
    }



    @Operation(summary = "댓글 조회", description = "그룹의 댓글을 조회합니다.")
    @GetMapping("/{groupId}/replies")
    public ApiPageResponse searchReplyRequest(@RequestParam Long cursor, @RequestParam Integer size, @PathVariable Long groupId){
        ReplySearchRequest request = new ReplySearchRequest(cursor, size);
        ReplySearchResponse result = replyService.searchReply(request, groupId);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
        //        return ApiResponse.success(result);
    }


    @Operation(summary = "댓글 생성", description = "로그인 필요, 그룹에 댓글을 입력합니다.")
    @PostMapping("/{groupId}/replies")
    public ApiResponse<Long> createReplyRequest(@Login Long userId,@RequestBody Content content, @PathVariable Long groupId){

        ReplySaveRequest request = new ReplySaveRequest(groupId, userId, null, content.getContent());
        Long saveReplyId = replyService.saveReply(request);

        return ApiResponse.success(saveReplyId);
    }

    @Operation(summary = "대댓글 조회", description = "댓글 Id를 기준으로 대댓글을 조회합니다.")
    @GetMapping("/{groupId}/replies/{replyId}")
    public ApiPageResponse searchReReplyReauest(@RequestParam Long cursor, @RequestParam Integer size, @PathVariable Long replyId){
        ReplySearchRequest request = new ReplySearchRequest(cursor, size);
        ReplySearchResponse result = replyService.searchReReply(request, replyId);
        return ApiPageResponse.success(result.getItems(), result.isHasNext(), result.getCursor());
    }

    @Operation(summary = "대댓글 생성", description = "로그인 필요, 대댓글을 생성합니다.")
    @PostMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<Long> createChildReplyRequest(@Login Long userId,@PathVariable Long groupId, @PathVariable Long replyId, @RequestBody Content content){
        ReplySaveRequest request = new ReplySaveRequest(groupId, userId, replyId, content.getContent());
        Long saveReplyId = replyService.saveReply(request);
        return ApiResponse.success(saveReplyId);
    }


    @Operation(summary = "댓글, 대댓글 수정", description = "댓글, 대댓글을 수정합니다.")
    @PatchMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<Void> updateReply(@Login Long userId, @PathVariable Long groupId, @PathVariable Long replyId, @RequestBody Content content){

        ReplyUpdateRequest request = new ReplyUpdateRequest(groupId, userId, replyId, content.getContent());
        replyService.updateReply(request);
        return ApiResponse.success();
    }

    @Operation(summary = "댓글,대댓글 삭제", description = "replyId를 기반으로 댓글 또는, 대댓글을 삭제합니다.")
    @DeleteMapping("/{groupId}/replies/{replyId}")
    public ApiResponse<Void> deleteReplyRequest(@Login Long userId, @PathVariable Long groupId, @PathVariable Long replyId){

        replyService.deleteByReplyId(replyId);

        return ApiResponse.success();
    }


    @Operation(summary = "그룹 참여자 조회", description = "그룹 참여자를 조회합니다.")
    @GetMapping("/{groupId}/participants")
    public ApiResponse<UserInfoList> findUserInfoOfGroup(@PathVariable Long groupId){
        List<UserInfo> result = groupService.findUserInfoOfGroup(groupId);
        UserInfoList userList = new UserInfoList(result);
        return ApiResponse.success(userList);
    }

    @Operation(summary = "그룹 대기요청 조회", description = "그룹 참여 요청 후 대기자 명단을 조회합니다.")
    @GetMapping("/{groupId}/waitinglist")
    public ApiResponse<List<UserInfo>> findWaitingList(@PathVariable Long groupId) {
        List<UserInfo> result = groupService.findWaitingListOfGroup(groupId);
        return ApiResponse.success(result);
    }
}
