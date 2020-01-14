package com.skysoft.business.api.service;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

public interface FriendService {

    void addAccountToFriends(FriendEntity friendEntity);

    ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser);

    ResponseEntity<Void> deleteFriend(DeleteFriendRequest request, CurrentUser currentUser);

    Optional<FriendEntity> getOptionalByAccountIdAndFriendIdAndFriendStatus(UUID accountId, UUID friendId, FriendStatus friendStatus);

    default FriendEntity getOneByAccountIdFriendIdAndFriendStatus(UUID accountId, UUID friendId, FriendStatus friendStatus){
        return getOptionalByAccountIdAndFriendIdAndFriendStatus(accountId, friendId, friendStatus).orElseThrow(
                () -> new NotFoundException("Friend not found"));
    }
}
