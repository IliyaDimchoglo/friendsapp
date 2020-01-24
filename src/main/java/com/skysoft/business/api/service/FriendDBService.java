package com.skysoft.business.api.service;

import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;

import java.util.List;
import java.util.Optional;

public interface FriendDBService {

    Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2);

    default FriendEntity getFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2) {
        return getOptionalFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(accountUsername, friendName, status1, friendUsername, accountName, status2).orElseThrow(() -> new NotFoundException("Friend not found"));
    }

    List<FriendEntity> getAllFriendsByAccountUsernameAndStatus(String currentUser, FriendStatus status);

    void save(FriendEntity friendEntity);

    Optional<FriendEntity> findOptionalByUsernameAndFriendNameAndStatus(String username, String friendName, FriendStatus status);

    default FriendEntity findByUsernameAndFriendNameAndStatus(String username, String friendName, FriendStatus status){
        return findOptionalByUsernameAndFriendNameAndStatus(username, friendName, status).orElseThrow(() -> new NotFoundException("Friend not found"));

    }

}
