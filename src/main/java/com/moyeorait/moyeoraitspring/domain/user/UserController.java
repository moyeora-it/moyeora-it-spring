package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @Autowired
    UserService userService;


//    @GetMapping("/{userId}")
//    public ApiResponse<?> findUserTest(@PathVariable Long userId){
//
//        String result = userService.findNodeUser(userId);
//        return ApiResponse.success(result);
//    }
//
//    @GetMapping("/userinfotest/{accessToken}")
//    public ApiResponse<?> findUserInfoTest(@PathVariable String accessToken){
//        String result = userService.testUserInfo(accessToken);
//        return ApiResponse.success(result);
//    }
}
