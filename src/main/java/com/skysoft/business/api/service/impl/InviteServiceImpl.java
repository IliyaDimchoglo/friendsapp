package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.AccountDto;
import com.skysoft.business.api.dto.IngoingInvitesDto;
import com.skysoft.business.api.dto.OutgoingInvitesDto;
import com.skysoft.business.api.dto.request.AddAccountToFriendsRequest;
import com.skysoft.business.api.dto.request.InvitationRequest;
import com.skysoft.business.api.dto.response.GetAllInvitationsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.FriendDBService;
import com.skysoft.business.api.service.InviteDBService;
import com.skysoft.business.api.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.FriendStatus.ACTIVE;
import static com.skysoft.business.api.model.FriendStatus.DELETED;
import static com.skysoft.business.api.model.InviteStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final AccountService accountService;
    private final InviteDBService inviteDBService;
    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Override
    public void sendInvite(AddAccountToFriendsRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        String friendName = request.getValidUsername(username);
        boolean pendingInvite = inviteDBService.existPendingInvite(username, friendName, PENDING);
        if (!pendingInvite && isFriends(username, friendName)) {
            inviteDBService.save(getInviteByAccountAndFriend(username, friendName));
            log.info("[x] Successful send invite to account: {}, for current user: {}", friendName, username);
        } else {
            log.warn("[x] Bad request account: {}, for invite: {}", username, friendName);
            throw new BadRequestException("Bad request for invite.");
        }
    }

    @Override
    public void acceptInvitation(InvitationRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        String friendName = request.getUsername();
        InviteEntity inviteFriendEntity = inviteDBService.getInviteByUsernameAndFriendNameAndStatusPending(friendName, username);// FIXME: 23.01.20 private method get friend
        addFriend(username, friendName, inviteFriendEntity);
        log.info("[x] New Friend: {}, successful added for account: {}.", friendName, username);
    }

    @Override
    public void rejectInvitation(InvitationRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        String friendName = request.getUsername();
        InviteEntity friendInviteEntity = inviteDBService.getInviteByUsernameAndFriendNameAndStatusPending(friendName, username);
        friendInviteEntity.setInviteStatus(REJECT);
        inviteDBService.save(friendInviteEntity);
        log.info("[x] Invite: {}, successful reject for account: {}.", friendName, username);
    }

    @Override
    public GetAllInvitationsResponse getAllInvitations(CurrentUser currentUser) {
        String username = currentUser.getUsername();
        IngoingInvitesDto ingoingInvites = getIngoingInvites(username);
        OutgoingInvitesDto outgoingInvites = getOutgoingInvites(username);
        log.info("[x] Get all invitations for current user: {}.", username);
        return new GetAllInvitationsResponse(ingoingInvites, outgoingInvites);
    }

    private void addFriend(String username, String friendName, InviteEntity entity) throws BadRequestException {
        Optional<FriendEntity> friend = friendDBService.findOptionalByUsernameAndFriendNameAndStatus(username, friendName, ACTIVE);// FIXME: 23.01.20 private method get friend
        if (!friend.isPresent()) {
            AccountEntity account = accountService.getAccountByUsername(username);
            AccountEntity friendAccount = accountService.getAccountByUsername(friendName);
            friendDBService.save(new FriendEntity(account, friendAccount));
            saveAcceptInviteStatus(entity);
        } else if (friend.get().getStatus().equals(DELETED)) {
            friend.get().setStatus(ACTIVE);
            friendDBService.save(friend.get());
        } else {
            log.warn("[x] Failed add friend: {}, for current user: {}", friendName, username);
            throw new BadRequestException("Friend already added.");
        }
    }

    private IngoingInvitesDto getIngoingInvites(String username) {
        List<AccountDto> namesList = inviteDBService.findAllInvitesByFriendUsernameAndStatus(username, PENDING)
                .stream()
                .map(InviteEntity::getAccount)
                .map(entity -> mapper.map(entity, AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get all ingoing Invitations: {}, for current user: {}.", namesList.toString(), username);
        return IngoingInvitesDto.of(namesList);
    }

    private OutgoingInvitesDto getOutgoingInvites(String username) {
        List<AccountDto> namesList = inviteDBService.findAllInvitesByAccountUsernameAndStatus(username, PENDING)
                .stream()
                .map(InviteEntity::getFriend)
                .map(entity -> mapper.map(entity, AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get all outgoing Invitations: {}, for current user: {}.", namesList.toString(), username);
        return OutgoingInvitesDto.of(namesList);
    }

    private InviteEntity getInviteByAccountAndFriend(String username, String friend) {
        AccountEntity accountEntity = accountService.getAccountByUsername(username);
        AccountEntity friendEntity = accountService.getAccountByUsername(friend);
        return InviteEntity.of(accountEntity, friendEntity, PENDING);
    }

    private void saveAcceptInviteStatus(InviteEntity entity) {
        entity.setInviteStatus(ACCEPT);
        inviteDBService.save(entity);
    }

    private boolean isFriends(String username, String friendName) {
        Optional<FriendEntity> friends = friendDBService.findOptionalByUsernameAndFriendNameAndStatus(friendName, username, ACTIVE);
        return !friends.isPresent();
    }
}
