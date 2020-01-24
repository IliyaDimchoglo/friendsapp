package com.skysoft.business.api.controller;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/all")
    public ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser) {
        GetAllFriendsResponse allFriends = friendService.getAllFriends(currentUser.getUsername());
        return ResponseEntity.ok(allFriends);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteFriend(@Valid @RequestBody DeleteFriendRequest request, CurrentUser currentUser) {
        friendService.deleteFriend(request, currentUser);
        return ResponseEntity.ok().build();
    }
}
