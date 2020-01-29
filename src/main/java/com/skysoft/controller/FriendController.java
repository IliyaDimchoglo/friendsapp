package com.skysoft.controller;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.request.DeleteFriendRequest;
import com.skysoft.dto.response.GetAllFriendsResponse;
import com.skysoft.service.FriendService;
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
        friendService.deleteFriend(request.getUsername(), currentUser.getUsername());
        return ResponseEntity.ok().build();
    }
}
