package com.skysoft.controller;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.request.AddAccountToFriendsRequest;
import com.skysoft.dto.request.InvitationRequest;
import com.skysoft.dto.response.GetAllInvitationsResponse;
import com.skysoft.service.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
public class InviteController {

    private final InviteService inviteService;

    @GetMapping
    public ResponseEntity<GetAllInvitationsResponse> getAllInvitations(CurrentUser currentUser) {
        GetAllInvitationsResponse allInvitations = inviteService.getAllInvitations(currentUser.getUsername());
        return ResponseEntity.ok(allInvitations);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendInvite(@Valid @RequestBody AddAccountToFriendsRequest request, CurrentUser currentUser) {
        String friendName = request.getValidUsername(currentUser.getUsername());
        inviteService.sendInvite(friendName, currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.acceptInvitation(request.getUsername(), currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.rejectInvitation(request.getUsername(), currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelInvite(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.cancelInvite(request.getUsername(), currentUser.getUsername());
        return ResponseEntity.ok().build();
    }
}
