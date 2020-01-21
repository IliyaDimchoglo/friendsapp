package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {

    List<FriendEntity> findFriendEntitiesByAccount_UsernameAndStatusOrFriend_UsernameAndStatus(String username, FriendStatus accountStatus, String friendName, FriendStatus status);

    Optional<FriendEntity> findFriendEntityByAccount_UsernameAndFriend_UsernameAndStatusOrFriend_UsernameAndAccount_UsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2);

    FriendEntity findFirstByFriend_Username(String username);
}
