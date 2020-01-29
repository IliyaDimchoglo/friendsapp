package com.skysoft.service;

import com.skysoft.dto.response.GetAllFriendsResponse;

public interface FriendService {

    GetAllFriendsResponse getAllFriends(String username);

    void deleteFriend(String friendName, String username);

}
