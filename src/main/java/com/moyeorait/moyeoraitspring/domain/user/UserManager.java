package com.moyeorait.moyeoraitspring.domain.user;

import com.moyeorait.moyeoraitspring.commons.exception.CustomException;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeMyInfoResponse;
import com.moyeorait.moyeoraitspring.commons.external.dto.NodeUserInfoResponse;
import com.moyeorait.moyeoraitspring.domain.user.dto.UserInfo;
import com.moyeorait.moyeoraitspring.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
        ResponseEntity<NodeUserInfoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                NodeUserInfoResponse.class
        );

        NodeUserInfoResponse result = response.getBody();
        if(result == null || !result.getStatus().isSuccess()){
            throw new CustomException(UserException.USER_INFO_NOT_FOUND);
        }
        log.debug("result : {}", result);
        UserInfo userInfo = UserInfo.of(result, userId);
        return userInfo;
    }

    public String findUserInfoByTokenAndNode(String token) {
        log.debug("findUserOfNodeServer token:" , token);
        String myInfoUrl = nodeBaseUrl + "info";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "accessToken=" + token);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<NodeMyInfoResponse> response = restTemplate.exchange(
                    myInfoUrl,
                    HttpMethod.GET,
                    entity,
                    NodeMyInfoResponse.class
            );
            NodeMyInfoResponse result = response.getBody();
            log.debug("result : {}", result);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                String userId = response.getBody().getItems().getItems().getId();
                return userId;
            }

        } catch (Exception e){

            log.error(e.getMessage());
            throw new CustomException(UserException.USER_AUTHORIZE_EXCEPTION);
        }
        return null;
    }

    public List<Long> findFollowers(Long userId) {

        return null;
    }
}
