package com.skysoft.business.api.service;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.AddAccountToFriendsRequest;
import com.skysoft.business.api.dto.request.InvitationRequest;
import com.skysoft.business.api.dto.response.GetAllInvitationsResponse;

public interface InviteService {

    GetAllInvitationsResponse getAllInvitations(CurrentUser currentUser);

    void acceptInvitation(InvitationRequest request, CurrentUser currentUser);

    void sendInvite(AddAccountToFriendsRequest request, CurrentUser currentUser);

    void rejectInvitation(InvitationRequest request, CurrentUser currentUser);

    void cancelInvite(InvitationRequest request, CurrentUser currentUser);
}
