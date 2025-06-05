package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse2;
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
    public static final String NODE_USERINFO_URL = "https://my-api.sjcpop.com/api/v1/user/";

    @Autowired
    RestTemplate restTemplate;
    public UserInfo findNodeUser(Long userId){
        String url = NODE_USERINFO_URL+userId;
        log.debug("url : {}", url);
        ResponseEntity<NodeUserInfoResponse2> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                NodeUserInfoResponse2.class
        );

        NodeUserInfoResponse2 result = response.getBody();
        log.debug("result : {}", result);
        UserInfo userInfo = UserInfo.of(result, userId);
        return userInfo;
    }
}
