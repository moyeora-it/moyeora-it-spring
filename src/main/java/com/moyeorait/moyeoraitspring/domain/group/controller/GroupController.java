package com.moyeorait.moyeoraitspring.domain.group.controller;

//import com.moyeorait.moyeoraitspring.commons.annotation.Login;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.group.GroupJoinManager;
import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.controller.response.GroupInfoResponse;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/groups")
@Slf4j
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupJoinManager groupJoinManager;


    @PostMapping
    public ApiResponse<Void> requestCreateGroup(@Valid @RequestBody CreateGroupRequest request){
        Long userId = 1L;

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

    @PostMapping("/{groupId}/applications")
    public ApiResponse<Void> joinRequestGroup(@PathVariable Long groupId){
        Long userId = 1L;

        groupJoinManager.joinRequest(groupId, userId);

        return ApiResponse.success();
    }

    @DeleteMapping("/{groupId}/applications")
    public ApiResponse<Void> leaveRequestGroup(@PathVariable Long groupId){
        Long userId = 1L;
        groupJoinManager.cancelRequest(groupId, userId);

        return ApiResponse.success();
    }

    //팀원 목록 보여주기
//    @PostMapping("/{groupId}/participants")
//    public ApiResponse<>
}
