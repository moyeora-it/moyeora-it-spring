package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserManager {
    public static final String NODE_USERINFO_URL = "http://34.47.97.152/api/v1/user/";

    @Autowired
    RestTemplate restTemplate;
    public UserInfo findNodeUser(Long userId){
        String url = NODE_USERINFO_URL+userId;

        ResponseEntity<NodeUserInfoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                NodeUserInfoResponse.class
        );

        NodeUserInfoResponse result = response.getBody();

        UserInfo userInfo = UserInfo.of(result, userId);
        log.debug("result : {}", result);
        return userInfo;
    }
}
