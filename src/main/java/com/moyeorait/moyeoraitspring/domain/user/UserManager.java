package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse2;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManager {

    @Value("${external.node.base-url}")
    private String nodeBaseUrl;

    private final RestTemplate restTemplate;

    public UserInfo findNodeUser(Long userId){
        String url = nodeBaseUrl + userId;
        log.debug("url : {}", url);
        ResponseEntity<NodeUserInfoResponse2> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                NodeUserInfoResponse2.class
        );

        NodeUserInfoResponse2 result = response.getBody();
        if(result == null || !result.getStatus().isSuccess()){
            throw new CustomException(UserException.USER_INFO_NOT_FOUND);
        }
        log.debug("result : {}", result);
        UserInfo userInfo = UserInfo.of(result, userId);
        return userInfo;
    }
}
