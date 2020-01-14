package com.skysoft.business.api.controller;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/get_all")
    public ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser) {
        return friendService.getAllFriends(currentUser);
    }

    @PostMapping("delete_friend")
    public ResponseEntity<Void> deleteFriend(@RequestBody DeleteFriendRequest request, CurrentUser currentUser) {
        return friendService.deleteFriend(request, currentUser);
    }
}
