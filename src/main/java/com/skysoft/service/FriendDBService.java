package com.skysoft.service;

import com.skysoft.exception.NotFoundException;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendDBService {

    Optional<FriendEntity> findActiveByUsernameAndFriendName(String accountUsername, String friendName);

    default FriendEntity getActiveByUsernameAndFriendName(String accountUsername, String friendName) {
        return findActiveByUsernameAndFriendName(accountUsername, friendName).orElseThrow(() -> new NotFoundException("Friend not found"));
    }

    List<FriendEntity> getAllFriendsByUsernameAndStatus(String currentUser, FriendStatus status);

    void save(FriendEntity friendEntity);

    boolean existByUsernameAndFriendNameAndStatusActive(String username, String friendName);

    Optional<FriendEntity> findByUsernameAndFriendName(String username, String friendName);

}
