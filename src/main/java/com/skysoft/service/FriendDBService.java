package com.skysoft.service;

import com.skysoft.exception.NotFoundException;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;

import java.util.List;
import java.util.Optional;

public interface FriendDBService {

    Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status);

    default FriendEntity getFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status) {
        return getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(accountUsername, friendName, status).orElseThrow(() -> new NotFoundException("Friend not found"));
    }

    List<FriendEntity> getAllFriendsByAccountUsernameAndStatus(String currentUser, FriendStatus status);

    void save(FriendEntity friendEntity);

    Optional<FriendEntity> findOptionalByUsernameAndFriendName(String username, String friendName);

    default FriendEntity findByUsernameAndFriendNameAndStatus(String username, String friendName){
        return findOptionalByUsernameAndFriendName(username, friendName).orElseThrow(() -> new NotFoundException("Friend not found"));

    }

}
