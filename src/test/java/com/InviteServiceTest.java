package com;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.request.AddAccountToFriendsRequest;
import com.skysoft.dto.request.InvitationRequest;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import com.skysoft.model.InviteEntity;
import com.skysoft.service.AccountDBService;
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
    private AccountDBService accountService;

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
        when(inviteDBService.existPendingInvite(username, friendName)).thenReturn(false);
        when(accountService.getByUsername(username)).thenReturn(new AccountEntity());
        doNothing().when(inviteDBService).save(any());

        inviteService.sendInvite(friendName, username);

        verify(inviteService, times(1)).sendInvite(friendName, username);
        verify(inviteDBService, times(1)).existPendingInvite(username, friendName);
    }

    @Test
    public void failSendInvite() {
        when(inviteDBService.existPendingInvite(username, friendName)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> inviteService.sendInvite(friendName, username));
    }

    @Test
    public void acceptInvitationWhenFriendsNotExist() {
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());
        when(friendDBService.existByUsernameAndFriendNameAndStatusActive(username, friendName)).thenReturn(true);
        doNothing().when(inviteDBService).save(any());
        when(accountService.getByUsername(username)).thenReturn(new AccountEntity());
        doNothing().when(friendDBService).save(any());

        inviteService.acceptInvitation(friendName, username);

        verify(inviteService, times(1)).acceptInvitation(friendName, username);
    }

    @Test
    public void acceptInvitationWhenFriendsDeleted() {
          FriendEntity entity = new FriendEntity();
        entity.setStatus(FriendStatus.DELETED);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());
        when(friendDBService.existByUsernameAndFriendNameAndStatusActive(username, friendName)).thenReturn(false);

        inviteService.acceptInvitation(friendName, username);

        verify(inviteService, times(1)).acceptInvitation(friendName, username);
    }

    @Test
    public void failAcceptInvitation() {
        assertThrows(Exception.class, () -> inviteService.acceptInvitation(any(), any()));
    }

    @Test
    public void rejectInvitationTest() {
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());

        inviteService.rejectInvitation(friendName, username);

        verify(inviteService, times(1)).rejectInvitation(friendName, username);
    }

    @Test
    public void failRejectInvitation() {
        assertThrows(Exception.class, () ->
            inviteService.rejectInvitation(randomUUID().toString(), username)
        );
    }

    @Test
    public void cancelInviteTest() {
        InvitationRequest request = new InvitationRequest(friendName);
        CurrentUser user = new CurrentUser(username);
        when(inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(any(), any())).thenReturn(new InviteEntity());

        inviteService.cancelInvite(friendName, username);

        verify(inviteService, times(1)).cancelInvite(friendName, username);
        verify(inviteDBService, times(1)).getInviteByUsernameAndFriendNameInStatusPending(any(), any());
    }

    @Test
    public void failCancelInviteTest() {
        assertThrows(Exception.class, () -> inviteService.cancelInvite(any(), any()));
    }

    @Test
    public void getAllInvitationsTest() {
        when(inviteDBService.getPendingInvitesByFriendName(username)).thenReturn(Collections.singletonList(new InviteEntity()));
        when(inviteDBService.getPendingInvitesByUsername(username)).thenReturn(Collections.singletonList(new InviteEntity()));

        inviteService.getAllInvitations(username);

        verify(inviteDBService, times(1)).getPendingInvitesByFriendName(username);
        verify(inviteDBService, times(1)).getPendingInvitesByUsername(username);
        verify(inviteService, times(1)).getAllInvitations(username);
    }
}
