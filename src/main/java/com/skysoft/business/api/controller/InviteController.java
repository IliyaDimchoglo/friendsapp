package com.skysoft.business.api.controller;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.AddAccountToFriendsRequest;
import com.skysoft.business.api.dto.request.InvitationRequest;
import com.skysoft.business.api.dto.response.GetAllInvitationsResponse;
import com.skysoft.business.api.service.InviteService;
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
        GetAllInvitationsResponse allInvitations = inviteService.getAllInvitations(currentUser);
        return ResponseEntity.ok(allInvitations);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendInvite(@Valid @RequestBody AddAccountToFriendsRequest request, CurrentUser currentUser) {
        inviteService.sendInvite(request, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.acceptInvitation(request, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectInvitation(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.rejectInvitation(request, currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelInvite(@Valid @RequestBody InvitationRequest request, CurrentUser currentUser) {
        inviteService.cancelInvite(request, currentUser);
        return ResponseEntity.ok().build();
    }
}
