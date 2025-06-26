package com.moyeorait.moyeoraitspring.domain.group;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.participant.service.ParticipantService;
import com.moyeorait.moyeoraitspring.domain.user.notification.NotificationManager;
import com.moyeorait.moyeoraitspring.domain.waitinglist.service.WaitingListService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupJoinManagerTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    WaitingListService waitingListService;

    @Autowired
    GroupService groupService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    GroupJoinManager groupJoinManager;

    @PersistenceContext
    EntityManager entityManager;

    @MockitoBean
    NotificationManager notificationManager;

    @Test
    @DisplayName("사용자가 그룹 참여를 취소한다.")
    void cancleCandidateSuccess(){
        Long userId = 1L;
        Long participantUserId = 2L;
        CreateGroupRequest request = new CreateGroupRequest(
                "스터디 모집합니다",
                LocalDateTime.of(2025, 6, 10, 23, 59),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                LocalDateTime.of(2025, 8, 15, 0, 0),
                5,
                "백엔드 중심의 스터디입니다.",
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3),
                "스터디",
                true
        );
        Long saveGroupId = groupService.createGroup(request, userId);
        Group savetGroup = groupService.findById(saveGroupId);

        Participant participant = new Participant(savetGroup, participantUserId);
        participantRepository.save(participant);

        entityManager.flush();
        entityManager.clear();

        groupJoinManager.cancelRequest(saveGroupId.longValue(), participantUserId.longValue());

        Optional<Participant> findParticipant = participantRepository.findById(participant.getParticipantId());
        Assertions.assertThat(findParticipant).isEmpty();
    }

    @Test
    @DisplayName("유저 그룹 동시 참여 방지 테스트")
    void userGroupPreventConcurrentParticapant() throws InterruptedException {
        CreateGroupRequest request = new CreateGroupRequest(
                "스터디 모집합니다",
                LocalDateTime.of(2025, 6, 10, 23, 59),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                LocalDateTime.of(2025, 8, 15, 0, 0),
                2,
                "백엔드 중심의 스터디입니다.",
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3),
                "스터디",
                true
        );
        Long user1 = 10001L;

        Long saveGroupId = groupService.createGroup(request, user1);
        Thread.sleep(100);
        Group group = groupService.findById(saveGroupId);
        System.out.println("group : " +group.toString());

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(long i = 1; i <= threadCount; i++){
            long userId = i+1;
            executorService.submit(() -> {
                try{
                    groupJoinManager.joinRequest(saveGroupId, userId);
                }catch (Exception e){
                    System.out.println("예외 발생 : " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        entityManager.flush();
        entityManager.clear(); // 1차 캐시 무효화
        // then
        Group updated = groupService.findById(saveGroupId);
        List<Participant> participantList = participantRepository.findByGroup(updated);


        assertEquals(2, updated.getCurrentParticipants()); // 최대 인원이 1명만 허용되므로
    }
}