package com.skysoft.service.impl;

import com.skysoft.dto.AccountDto;
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
    @Transactional
    public void sendInvite(AddAccountToFriendsRequest request, String currentUser) {
        String friendName = request.getValidUsername(currentUser);
        boolean pendingInvite = inviteDBService.existPendingInvite(currentUser, friendName);
        boolean isFriends = friendDBService.existByUsernameAndFriendNameAndStatusActive(friendName, currentUser);
        if (!pendingInvite && !isFriends) {
            inviteDBService.save(createInviteByAccountAndFriend(currentUser, friendName));
            log.info("[x] Successful send invite account2 account1: {}, for current user: {}", friendName, currentUser);
        } else {
            log.warn("[x] Bad request account1: {}, for invite: {}", currentUser, friendName);
            throw new BadRequestException("Bad request for invite.");
        }
    }

    @Override
    @Transactional
    public void acceptInvitation(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        InviteEntity inviteFriendEntity = getPendingInviteByAccountAndFriend(friendName, currentUser);
        addFriend(currentUser, friendName, inviteFriendEntity);
        log.info("[x] New Friend: {}, successful added for account1: {}.", friendName, currentUser);
    }

    @Override
    @Transactional
    public void rejectInvitation(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        InviteEntity friendInviteEntity = getPendingInviteByAccountAndFriend(friendName, currentUser);
        friendInviteEntity.setInviteStatus(REJECT);
        log.info("[x] Invite: {}, successful reject for account1: {}.", friendName, currentUser);
    }

    @Override
    @Transactional
    public void cancelInvite(InvitationRequest request, String currentUser) {
        String friendName = request.getUsername();
        try {
            InviteEntity inviteEntity = getPendingInviteByAccountAndFriend(currentUser, friendName);
            inviteEntity.setInviteStatus(CANCEL);
            log.info("Cancel invite account2: {}, account1 user: {}.", friendName, currentUser);
        } catch (Exception e){
            log.warn("[x] Bad request for cancel invite account1 user: {}, account2: {}", currentUser, friendName);
            throw new BadRequestException("Bad request for cancel invite.");
        }
    }

    @Override
    public GetAllInvitationsResponse getAllInvitations(String currentUser) {
        List<AccountDto> ingoingInvites = getIngoingInvites(currentUser);
        List<AccountDto> outgoingInvites = getOutgoingInvites(currentUser);
        log.info("[x] Get all invitations for current user: {}.", currentUser);
        return new GetAllInvitationsResponse(ingoingInvites, outgoingInvites);
    }

    private void addFriend(String username, String friendName, InviteEntity entity) throws BadRequestException {
        Optional<FriendEntity> friend = friendDBService.findByUsernameAndFriendName(username, friendName);
        if (!friend.isPresent()) {
            AccountEntity account = accountService.getAccountByUsername(username);
            AccountEntity friendAccount = accountService.getAccountByUsername(friendName);
            friendDBService.save(new FriendEntity(account, friendAccount));
            entity.acceptInvite();
        } else if (friend.get().getStatus().equals(DELETED)) {
            friend.get().setStatus(ACTIVE);
        } else {
            log.warn("[x] Failed add account2: {}, for current user: {}", friendName, username);
            throw new BadRequestException("Friend already added.");
        }
    }

    private List<AccountDto> getIngoingInvites(String username) {
            List<AccountDto> namesList = inviteDBService.findAllInvitesByFriendUsernameAndStatus(username, PENDING)
                    .stream()
                    .map(entity -> mapper.map(entity.getAccount1(), AccountDto.class))
                    .collect(Collectors.toList());
            log.info("[x] Get all ingoing Invitations: {}, for current user: {}.", namesList.toString(), username);
            return namesList;
    }

    private List<AccountDto> getOutgoingInvites(String username) {
        List<AccountDto> namesList = inviteDBService.findAllInvitesByAccountUsernameAndStatus(username, PENDING)
                .stream()
                .map(entity -> mapper.map(entity.getAccount2(), AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get all outgoing Invitations: {}, for current user: {}.", namesList.toString(), username);
        return namesList;
    }

    private InviteEntity getPendingInviteByAccountAndFriend(String friendName, String username) {
        return inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(friendName, username);
    }

    private InviteEntity createInviteByAccountAndFriend(String username, String friend) {
        AccountEntity accountEntity = accountService.getAccountByUsername(username);
        AccountEntity friendEntity = accountService.getAccountByUsername(friend);
        return InviteEntity.of(accountEntity, friendEntity, PENDING);
    }
}
