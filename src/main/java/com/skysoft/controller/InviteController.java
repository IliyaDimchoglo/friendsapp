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
        String user = currentUser.getUsername();
        GetAllInvitationsResponse allInvitations = inviteService.getAllInvitations(user);
        return ResponseEntity.ok(allInvitations);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendInvite(@Valid @RequestBody AddAccountToFriendsRequest request, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        inviteService.sendInvite(request, user);// FIXME: 28.01.20 validate here
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        inviteService.acceptInvitation(request, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        inviteService.rejectInvitation(request, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelInvite(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        inviteService.cancelInvite(request, user);
        return ResponseEntity.ok().build();
    }
}
