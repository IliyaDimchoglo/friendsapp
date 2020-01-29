package com.skysoft.repository;

import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {

    List<FriendEntity> findFriendEntitiesByAccount1_UsernameAndStatusOrAccount2_UsernameAndStatus(String username, FriendStatus accountStatus, String friendName, FriendStatus status);

    Optional<FriendEntity> findFriendEntityByAccount1_UsernameAndAccount2_UsernameAndStatusOrAccount2_UsernameAndAccount1_UsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2);

    boolean existsByAccount1_UsernameAndAccount2_UsernameAndStatusOrAccount2_UsernameAndAccount1_UsernameAndStatus(String username, String friendName, FriendStatus status, String friendUsername, String name, FriendStatus status2);

    Optional<FriendEntity> findFriendEntityByAccount1_UsernameAndAccount2_UsernameOrAccount2_UsernameAndAccount1_Username(String username, String friendName, String name, String friendUsername);
}
