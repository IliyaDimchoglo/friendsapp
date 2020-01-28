package com.skysoft.repository;

import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {

    List<FriendEntity> findFriendEntitiesByAccount_UsernameAndStatusOrFriend_UsernameAndStatus(String username, FriendStatus accountStatus, String friendName, FriendStatus status);

    Optional<FriendEntity> findFriendEntityByAccountUsernameAndFriendUsernameAndStatusOrFriendUsernameAndAccountUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2);

    Optional<FriendEntity> findFirstByAccount_UsernameAndFriend_UsernameOrFriend_UsernameAndAccount_Username(String username, String friendName, String friendUsername, String name);

}
