package com.moyeorait.moyeoraitspring.commons.test;

import com.moyeorait.moyeoraitspring.commons.annotation.Login;
import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.response.ApiResponse;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    UserManager userManager;
//    @GetMapping("/test")
//    public String test(){
//
//        return "test";
//    }
//
//    @GetMapping("/test2")
//    public String test2(@Login Long userId){
//        System.out.println("userId : " + userId);
//        return "test";
//    }
//
//    @GetMapping("/test3")
//    public String test3(){
//
//        return "test3";
//    }

//    @GetMapping("/test4/{userId}")
//    public void test4(@PathVariable Long userId){
//        log.debug("test4");
//
//        userManager.findNodeUser(userId);
//
//    }
}
