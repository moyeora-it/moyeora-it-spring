//package com.moyeorait.moyeoraitspring.domain.group.controller;
//
////import com.moyeorait.moyeoraitspring.commons.annotation.Login;
//import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
//import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/v2/group")
//@Slf4j
//public class GroupController {
//
//    @PostMapping
//    public ApiResponse<Void> requestCreateGroup(@Valid @RequestBody CreateGroupRequest request){
//        System.out.println("test");
//        log.debug("request : {}", request);
//
//        return ApiResponse.success();
//    }
//}
