package com.skysoft.service.impl;

import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import com.skysoft.repository.FriendRepository;
import com.skysoft.service.FriendDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendDBServiceImpl implements FriendDBService {

    private final FriendRepository friendRepository;

    @Override
    public Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status) {
        return friendRepository.findFriendEntityByAccountUsernameAndFriendUsernameAndStatusOrFriendUsernameAndAccountUsernameAndStatus(accountUsername, friendName, status, accountUsername, friendName, status);
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
    public Optional<FriendEntity> findOptionalByUsernameAndFriendName(String username, String friendName) {
        return friendRepository.findFirstByAccount_UsernameAndFriend_UsernameOrFriend_UsernameAndAccount_Username(username, friendName, username, friendName);
    }
}
