package com.moyeorait.moyeoraitspring.domain.user.notification;

import com.moyeorait.moyeoraitspring.commons.enumdata.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationManager {
    private static final String NOTIFICATION_API_URL = "https://my-api.sjcpop.com/api/v1/notification/spring";


    private final RestTemplate restTemplate;

    private static final Map<NotificationType, NotificationTemplate> templateMap = Map.ofEntries(
            Map.entry(NotificationType.GROUP_HAS_PARTICIPANT, new NotificationTemplate("새로운 참가자가 그룹에 참여했습니다.", "/groups/%d/participants")),
            Map.entry(NotificationType.CONFIRMED_PARTICIPANT_CANCELED, new NotificationTemplate("확정된 참가자가 취소했습니다.", "/groups/%d")),
            Map.entry(NotificationType.COMMENT_RECEIVED, new NotificationTemplate("새로운 댓글이 달렸습니다.", "/groups/%d#comment-%d")),
            Map.entry(NotificationType.APPLY_APPROVED,
                    new NotificationTemplate("모임 참가 신청이 승인되었습니다.", "/groups/%d")),

            Map.entry(NotificationType.APPLY_REJECTED,
                    new NotificationTemplate("모임 참가 신청이 거절되었습니다.", null)),

            Map.entry(NotificationType.FULL_CAPACITY,
                    new NotificationTemplate("모임 정원이 마감되었습니다.", "/groups/%d")),

            Map.entry(NotificationType.FOLLOWER_ADDED,
                    new NotificationTemplate("새로운 팔로워가 추가되었습니다.", "/users/%d")),

            Map.entry(NotificationType.APPLY_CANCELED,
                    new NotificationTemplate("모임 신청이 취소되었습니다.", null)),

            Map.entry(NotificationType.FOLLOWER_CREATE_GROUP,
                    new NotificationTemplate("팔로우하는 사용자가 새 모임을 만들었습니다.", "/groups/%d")),

            Map.entry(NotificationType.REJECTED_GROUP,
                    new NotificationTemplate("모임 신청이 거절되었습니다.", null)),

            Map.entry(NotificationType.WITHIN_24_HOUR,
                    new NotificationTemplate("북마크한 모임이 24시간 후에 시작됩니다.", "/groups/%d"))
    );

    public void sendNotification(NotificationType type, Long targetUserId, String targetUrl) {
        NotificationTemplate template = templateMap.get(type);
        if (template == null) throw new IllegalArgumentException("정의되지 않은 알림 타입입니다: " + type);

        String message = template.message();
        String url = targetUrl; // args: groupId, commentId 등

        NotificationRequest request = new NotificationRequest(targetUserId, type.name(), message, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(NOTIFICATION_API_URL, entity, String.class);
            log.info("알림 전송 응답: {}", response.getBody());
        } catch (Exception e) {
            log.error("알림 전송 실패", e);
        }
    }
}
