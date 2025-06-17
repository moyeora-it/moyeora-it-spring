package com.moyeorait.moyeoraitspring.commons;

import com.moyeorait.moyeoraitspring.commons.enumdata.NotificationType;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.Bookmark;
import com.moyeorait.moyeoraitspring.domain.bookmark.repository.BookmarkRepository;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.repository.GroupRepository;
import com.moyeorait.moyeoraitspring.domain.user.notification.NotificationManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationManager notificationManager;
    private final GroupRepository groupRepository;
    private final BookmarkRepository bookmarkRepository;


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void notificationSend(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.plusHours(23).plusMinutes(59);
        LocalDateTime to = now.plusHours(24);
        log.debug("from : {}", from);
        log.debug("to : {}", to);

        List<Group> groups = groupRepository.findByStartDateBetween(from, to);
        for(Group g : groups){
            List<Bookmark> bookmarks = bookmarkRepository.findByGroup(g);
            for(Bookmark b : bookmarks){
                Long userId = b.getUserId();
                String url = String.format("/groups/%d", g.getGroupId());
                notificationManager.sendNotification(
                        NotificationType.WITHIN_24_HOUR,
                        userId,
                        url
                );
            }
        }
        log.debug("{}개의 게시글에 대해 알림 요청이 완료되었습니다.", groups.size());
    }

}
