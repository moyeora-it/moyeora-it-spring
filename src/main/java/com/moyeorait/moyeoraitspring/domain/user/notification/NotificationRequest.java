package com.moyeorait.moyeoraitspring.domain.user.notification;

public record NotificationRequest(
        Long targetUserId,
        String notificationType,
        String message,
        String url
) {
}
