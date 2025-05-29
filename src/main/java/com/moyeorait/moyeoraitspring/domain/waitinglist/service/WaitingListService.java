package com.moyeorait.moyeoraitspring.domain.waitinglist.service;

import com.moyeorait.moyeoraitspring.domain.group.repository.Group;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingList;
import com.moyeorait.moyeoraitspring.domain.waitinglist.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WaitingListService {
    @Autowired
    WaitingListRepository waitingListRepository;

    public void addUserToGroup(Group group, Long userId) {
        WaitingList waitingList = new WaitingList(group, userId);
        waitingListRepository.save(waitingList);
    }
}
