package com.skysoft.service;

import com.skysoft.dto.response.GetAllInvitationsResponse;

public interface InviteService {

    GetAllInvitationsResponse getAllInvitations(String username);

    void acceptInvitation(String friendName, String username);

    void sendInvite(String friendName, String username);

    void rejectInvitation(String friendName, String username);

    void cancelInvite(String friendName, String username);
}
