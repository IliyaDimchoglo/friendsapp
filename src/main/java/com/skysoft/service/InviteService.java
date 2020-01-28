package com.skysoft.service;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.request.AddAccountToFriendsRequest;
import com.skysoft.dto.request.InvitationRequest;
import com.skysoft.dto.response.GetAllInvitationsResponse;

public interface InviteService {

    GetAllInvitationsResponse getAllInvitations(String currentUser);

    void acceptInvitation(InvitationRequest request, String currentUser);

    void sendInvite(AddAccountToFriendsRequest request, String currentUser);

    void rejectInvitation(InvitationRequest request, String currentUser);

    void cancelInvite(InvitationRequest request, String currentUser);
}
