package com.moyeorait.moyeoraitspring;

import com.moyeorait.moyeoraitspring.commons.enumdata.SkillEnum;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application.yml")
class MoyeoraItSpringApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    @Test
    void test(){
        List<Integer> testList = List.of(1, 2, 3);

        List<String> result = testList.stream()
                .map(idx -> SkillEnum.values()[idx].toString()).toList();

        for(String r : result){
            System.out.println("r : " + r);
        }

    }
}
