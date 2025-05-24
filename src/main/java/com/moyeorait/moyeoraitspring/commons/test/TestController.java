package com.moyeorait.moyeoraitspring.commons.test;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        throw new CustomException(TestException.TEST);
//        return "test";
    }
}
