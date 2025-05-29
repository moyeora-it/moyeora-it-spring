package com.moyeorait.moyeoraitspring.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {


    private final RestTemplate restTemplate;

    public UserNodeResponse getUserInfo(Long userId){
        String url = "testURL";
        return restTemplate.getForObject(url, UserNodeResponse.class);
    }
}
