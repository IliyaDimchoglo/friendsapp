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

    GetAllFriendsResponse getAllFriends(String currentUser);

    void deleteFriend(DeleteFriendRequest request, CurrentUser currentUser);

}
