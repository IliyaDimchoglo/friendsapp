package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import com.skysoft.business.api.repository.FriendRepository;
import com.skysoft.business.api.service.FriendDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendDBServiceImpl implements FriendDBService {

    private final FriendRepository friendRepository;

    @Override
    public Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2) {
        return friendRepository.findFriendEntityByAccount_UsernameAndFriend_UsernameAndStatusOrFriend_UsernameAndAccount_UsernameAndStatus(accountUsername, friendName, status1, friendUsername, accountName, status2);
    }

    @Override
    public List<FriendEntity> getAllFriendsByAccountUsernameAndStatus(String currentUser, FriendStatus status) {
        return friendRepository.findFriendEntitiesByAccount_UsernameAndStatusOrFriend_UsernameAndStatus(currentUser, status, currentUser, status);
    }

    @Override
    public void save(FriendEntity friendEntity) {
        friendRepository.save(friendEntity);
    }

    @Override
    public Optional<FriendEntity> findOptionalByUsernameAndFriendNameAndStatus(String username, String friendName, FriendStatus status) {
        return friendRepository.findFirstByAccount_UsernameAndFriend_UsernameAndStatusOrFriend_UsernameAndAccount_UsernameAndStatus(username, friendName,status, friendName, username,status);
    }
}
