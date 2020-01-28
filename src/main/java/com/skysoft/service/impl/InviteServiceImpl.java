package com.skysoft.service.impl;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.AccountDto;
import com.skysoft.dto.IngoingInvitesDto;
import com.skysoft.dto.OutgoingInvitesDto;
import com.skysoft.dto.request.AddAccountToFriendsRequest;
import com.skysoft.dto.request.InvitationRequest;
import com.skysoft.dto.response.GetAllInvitationsResponse;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.InviteEntity;
import com.skysoft.service.AccountService;
import com.skysoft.service.FriendDBService;
import com.skysoft.service.InviteDBService;
import com.skysoft.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.skysoft.model.FriendStatus.ACTIVE;
import static com.skysoft.model.FriendStatus.DELETED;
import static com.skysoft.model.InviteStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final AccountService accountService;
    private final InviteDBService inviteDBService;
    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Override
    public void sendInvite(AddAccountToFriendsRequest request, String currentUser) {
        String friendName = request.getValidUsername(currentUser);
        boolean pendingInvite = inviteDBService.existPendingInvite(currentUser, friendName, PENDING);
        if (!pendingInvite && !isFriends(currentUser, friendName)) {
            inviteDBService.save(createInviteByAccountAndFriend(currentUser, friendName));
            log.info("[x] Successful send invite to account: {}, for current user: {}", friendName, currentUser);
        } else {
            log.warn("[x] Bad request account: {}, for invite: {}", currentUser, friendName);
            throw new BadRequestException("Bad request for invite.");
        }
    }

    @Override
    public void acceptInvitation(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        InviteEntity inviteFriendEntity = getPendingInviteByAccountAndFriend(friendName, currentUser);// FIXME: 23.01.20 private method get friend
        addFriend(currentUser, friendName, inviteFriendEntity);
        log.info("[x] New Friend: {}, successful added for account: {}.", friendName, currentUser);
    }

    @Override
    @Transactional
    public void rejectInvitation(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        InviteEntity friendInviteEntity = getPendingInviteByAccountAndFriend(friendName, currentUser);
        friendInviteEntity.setInviteStatus(REJECT);
        log.info("[x] Invite: {}, successful reject for account: {}.", friendName, currentUser);
    }

    @Override
    @Transactional
    public void cancelInvite(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        try {
            InviteEntity inviteEntity = getPendingInviteByAccountAndFriend(currentUser, friendName);
            inviteEntity.setInviteStatus(CANCEL);
            log.info("Cancel invite to: {}, from user: {}.", friendName, currentUser);
        } catch (Exception e){
            log.warn("[x] Bad request for cancel invite from user: {}, to: {}", currentUser, friendName);
            throw new BadRequestException("Bad request for cancel invite.");
        }
    }

    @Override
    public GetAllInvitationsResponse getAllInvitations(String currentUser) {
        IngoingInvitesDto ingoingInvites = getIngoingInvites(currentUser);
        OutgoingInvitesDto outgoingInvites = getOutgoingInvites(currentUser);
        log.info("[x] Get all invitations for current user: {}.", currentUser);
        return new GetAllInvitationsResponse(ingoingInvites, outgoingInvites);
    }

    private void addFriend(String username, String friendName, InviteEntity entity) throws BadRequestException {
        Optional<FriendEntity> friend = friendDBService.findOptionalByUsernameAndFriendName(username, friendName);
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

    private InviteEntity getPendingInviteByAccountAndFriend(String friendName, String username) {
        return inviteDBService.getInviteByUsernameAndFriendNameAndStatusPending(friendName, username);
    }

    private InviteEntity createInviteByAccountAndFriend(String username, String friend) {
        AccountEntity accountEntity = accountService.getAccountByUsername(username);
        AccountEntity friendEntity = accountService.getAccountByUsername(friend);
        return InviteEntity.of(accountEntity, friendEntity, PENDING);
    }

    private void saveAcceptInviteStatus(InviteEntity entity) {
        entity.setInviteStatus(ACCEPT);
        inviteDBService.save(entity);
    }

    private boolean isFriends(String username, String friendName) {
        Optional<FriendEntity> friends = friendDBService.findOptionalByUsernameAndFriendName(friendName, username);
        return friends.isPresent() && friends.get().getStatus().equals(ACTIVE);
    }
}
