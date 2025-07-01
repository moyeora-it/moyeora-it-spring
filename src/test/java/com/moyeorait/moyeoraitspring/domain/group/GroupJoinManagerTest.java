package com.moyeorait.moyeoraitspring.domain.group;

import com.moyeorait.moyeoraitspring.domain.group.controller.request.CreateGroupRequest;
import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.group.service.GroupService;
import com.moyeorait.moyeoraitspring.domain.participant.ParticipantRepository;
import com.moyeorait.moyeoraitspring.domain.participant.repository.Participant;
import com.moyeorait.moyeoraitspring.domain.participant.service.ParticipantService;
import com.moyeorait.moyeoraitspring.domain.user.UserManager;
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
import org.springframework.test.context.transaction.TestTransaction;
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

    @MockitoBean
    UserManager userManager;

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
        Group savedGroup = groupService.findById(saveGroupId);

        Participant participant = new Participant(savedGroup, participantUserId);
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
                "스터디입니다.",
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3),
                "스터디",
                true
        );
        Long ownerId = 10001L;
        Long saveGroupId = groupService.createGroup(request, ownerId);


        TestTransaction.flagForCommit();
        TestTransaction.end();

        Group group = groupService.findById(saveGroupId);

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(long i = 0; i < threadCount; i++){
            long userId = 10002L + i;
            executorService.submit(() -> {
                try{
                    groupJoinManager.joinRequest(group.getGroupId(), userId);
                    System.out.println( userId + "번 유저 요청 성공");
                }catch (Exception e){
                    System.out.println("예외 발생 : " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 종료 대기

        TestTransaction.start();

        // then
        Group updated = groupService.findById(saveGroupId);
        List<Participant> participantList = participantRepository.findByGroup(updated);

        System.out.println("최종 참여자 수: " + updated.getCurrentParticipants());
        System.out.println("실제 참여자 수: " + participantList.size());
        for(Participant p : participantList){
            System.out.println(p.toString());
        }
        assertEquals(2, updated.getCurrentParticipants());
        assertEquals(2, participantList.size());

        groupService.deleteGroupByGroupId(updated.getGroupId());
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}