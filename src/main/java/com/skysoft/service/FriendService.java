package com.skysoft.service;

import com.skysoft.dto.request.DeleteFriendRequest;
import com.skysoft.dto.response.GetAllFriendsResponse;

public interface FriendService {

    GetAllFriendsResponse getAllFriends(String currentUser);

    void deleteFriend(DeleteFriendRequest request, String currentUser);

}
