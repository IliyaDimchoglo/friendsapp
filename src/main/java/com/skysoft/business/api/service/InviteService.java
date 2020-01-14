package com.skysoft.business.api.service;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.AddAccountToFriendsRequest;
import com.skysoft.business.api.dto.request.InvitationRequest;
import com.skysoft.business.api.dto.response.GetAllInvitationsResponse;
import org.springframework.http.ResponseEntity;

public interface InviteService {

    ResponseEntity<GetAllInvitationsResponse> getAllInvitations(CurrentUser currentUser);

    ResponseEntity<Void> acceptInvitation(InvitationRequest request, CurrentUser currentUser);

    void sendInvite(AddAccountToFriendsRequest request, CurrentUser currentUser);

    ResponseEntity<Void> rejectInvitation(InvitationRequest request, CurrentUser currentUser);
}
