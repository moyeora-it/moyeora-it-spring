package com.moyeorait.moyeoraitspring.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public String findNodeUser(Long userId) {
        String url = "http://34.47.97.152/api/v1/user/"+userId;
        return restTemplate.getForObject(url, String.class);
    }

    public String testUserInfo(String accessToken) {
        String url = "http://34.47.97.152/api/v1/user/info";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "accessToken=" + accessToken); // 쿠키에 accessToken 추가

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
