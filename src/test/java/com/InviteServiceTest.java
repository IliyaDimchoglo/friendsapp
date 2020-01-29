/*
package com;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.request.AddAccountToFriendsRequest;
import com.skysoft.dto.request.InvitationRequest;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import com.skysoft.model.InviteEntity;
import com.skysoft.service.AccountService;
import com.skysoft.service.FriendDBService;
import com.skysoft.service.InviteDBService;
import com.skysoft.service.impl.InviteServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static com.skysoft.model.InviteStatus.PENDING;
import static java.util.UUID.randomUUID;
import static org.junit.gen5.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InviteServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private InviteDBService inviteDBService;

    @Mock
    private FriendDBService friendDBService;

    @Mock
    private ModelMapper mapper;

    private String username;
    private String friendName;
    private AddAccountToFriendsRequest request;

    @Spy
    @InjectMocks
    private InviteServiceImpl inviteService;

    @Before
    public void setUp() {
        username = "bob";
        friendName = "pop";
        request = new AddAccountToFriendsRequest(friendName);
    }

    @Test
    public void sendInvite() {
        when(inviteDBService.existPendingInvite(username, friendName, PENDING)).thenReturn(false);
        when(accountService.getAccountByUsername(username)).thenReturn(new AccountEntity());
        doNothing().when(inviteDBService).save(any());

        inviteService.sendInvite(request, username);

        verify(inviteService, times(1)).sendInvite(request, username);
        verify(inviteDBService, times(1)).existPendingInvite(username, friendName, PENDING);
    }

    @Test
    public void failSendInvite() {
        when(inviteDBService.existPendingInvite(username, friendName, PENDING)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> inviteService.sendInvite(request, username));
    }

    @Test
    public void acceptInvitationWhenFriendsNotExist() {
        InvitationRequest request = new InvitationRequest(friendName);
        CurrentUser user = new CurrentUser(username);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());
        when(friendDBService.existByUsernameAndFriendNameAndStatusActive(username, friendName)).thenReturn(Optional.empty());
        doNothing().when(inviteDBService).save(any());
        when(accountService.getAccountByUsername(username)).thenReturn(new AccountEntity());
        doNothing().when(friendDBService).save(any());

        inviteService.acceptInvitation(request, username);

        verify(inviteService, times(1)).acceptInvitation(request, username);
    }

    @Test
    public void acceptInvitationWhenFriendsDeleted() {
        InvitationRequest request = new InvitationRequest(friendName);
        CurrentUser user = new CurrentUser(username);
        FriendEntity entity = new FriendEntity();
        entity.setStatus(FriendStatus.DELETED);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());
        when(friendDBService.existByUsernameAndFriendNameAndStatusActive(username, friendName)).thenReturn(Optional.of(entity));

        inviteService.acceptInvitation(request, username);

        verify(inviteService, times(1)).acceptInvitation(request, username);
    }

    @Test
    public void failAcceptInvitation() {
        assertThrows(Exception.class, () -> inviteService.acceptInvitation(any(), any()));
    }

    @Test
    public void rejectInvitationTest() {
        InvitationRequest request = new InvitationRequest(friendName);
        CurrentUser user = new CurrentUser(username);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());

        inviteService.rejectInvitation(request, username);

        verify(inviteService, times(1)).rejectInvitation(request, username);
    }

    @Test
    public void failRejectInvitation() {
        assertThrows(Exception.class, () -> {
            inviteService.rejectInvitation(new InvitationRequest(randomUUID().toString()), username);
        });
    }

    @Test
    public void cancelInviteTest() {
        InvitationRequest request = new InvitationRequest(friendName);
        CurrentUser user = new CurrentUser(username);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());

        inviteService.cancelInvite(request, username);

        verify(inviteService, times(1)).cancelInvite(request, username);
        verify(inviteDBService, times(1)).getInviteByUsernameAndFriendNameInStatusPending(any(), any());
    }

    @Test
    public void failCancelInviteTest() {
        assertThrows(Exception.class, () -> inviteService.cancelInvite(any(), any()));
    }

    @Test
    public void getAllInvitationsTest() {
        when(inviteDBService.getPendingInvitesByFriendName(username, PENDING)).thenReturn(Collections.singletonList(new InviteEntity()));
        when(inviteDBService.getAllInvitesUsernameAndStatus(username, PENDING)).thenReturn(Collections.singletonList(new InviteEntity()));

        inviteService.getAllInvitations(username);

        verify(inviteDBService, times(1)).getPendingInvitesByFriendName(username, PENDING);
        verify(inviteDBService, times(1)).getAllInvitesUsernameAndStatus(username, PENDING);
        verify(inviteService, times(1)).getAllInvitations(username);
    }
}
*/
