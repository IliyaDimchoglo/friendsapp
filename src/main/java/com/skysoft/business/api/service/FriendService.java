package com.skysoft.business.api.service;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface FriendService {

    ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser);

    ResponseEntity<Void> deleteFriend(DeleteFriendRequest request, CurrentUser currentUser);

    Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusAndFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2);

    default FriendEntity getFriendEntityByAccountUsernameAndStatusAndFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2) {
        return getOptionalFriendEntityByAccountUsernameAndStatusAndFriendUsernameAndStatus(accountUsername, friendName, status1, friendUsername, accountName, status2).orElseThrow(() -> new NotFoundException("Friend not found"));
    }

    void save(FriendEntity friendEntity);

    FriendEntity findByUsername(String username);
}
