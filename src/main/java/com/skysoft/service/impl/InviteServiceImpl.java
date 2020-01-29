package com.skysoft.service.impl;

import com.skysoft.dto.AccountDto;
import com.skysoft.dto.response.GetAllInvitationsResponse;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.model.InviteEntity;
import com.skysoft.service.AccountDBService;
import com.skysoft.service.FriendDBService;
import com.skysoft.service.InviteDBService;
import com.skysoft.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AccountDBService accountDBService;
    private final InviteDBService inviteDBService;
    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void sendInvite(String friendName, String username) {
        boolean pendingInvite = inviteDBService.existPendingInvite(username, friendName);
        boolean isFriends = friendDBService.existByUsernameAndFriendNameAndStatusActive(friendName, username);
        if (!pendingInvite && !isFriends) {
            createAndSaveInviteByUserAndFriend(username, friendName);
            log.info("[x] Successful send invite account2 account1: {}, for current user: {}", friendName, username);
        } else {
            log.warn("[x] Bad request user: {}, for invite: {}", username, friendName);
            throw new BadRequestException("Bad request for invite.");
        }
    }

    @Override
    @Transactional
    public void acceptInvitation(String friendName, String username) {
        InviteEntity inviteFriendEntity = getPendingInviteByUserAndFriend(friendName, username);
        addFriend(username, friendName, inviteFriendEntity);
        log.info("[x] New Friend: {}, successful added for user: {}.", friendName, username);
    }

    @Override
    @Transactional
    public void rejectInvitation(String friendName, String username) {
        InviteEntity friendInviteEntity = getPendingInviteByUserAndFriend(friendName, username);
        friendInviteEntity.setInviteStatus(REJECT);
        log.info("[x] Invite: {}, successful reject for user: {}.", friendName, username);
    }

    @Override
    @Transactional
    public void cancelInvite(String friendName, String username) {
        try {
            InviteEntity inviteEntity = getPendingInviteByUserAndFriend(username, friendName);
            inviteEntity.setInviteStatus(CANCEL);
            log.info("Cancel invite friend: {}, for user: {}.", friendName, username);
        } catch (Exception e){
            log.warn("[x] Bad request for cancel invite user: {}, friend: {}", username, friendName);
            throw new BadRequestException("Bad request for cancel invite.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllInvitationsResponse getAllInvitations(String username) {
        List<AccountDto> ingoingInvites = getIngoingInvites(username);
        List<AccountDto> outgoingInvites = getOutgoingInvites(username);
        log.info("[x] Get all invitations for current user: {}.", username);
        return new GetAllInvitationsResponse(ingoingInvites, outgoingInvites);
    }

    private void addFriend(String username, String friendName, InviteEntity entity) throws BadRequestException {
        Optional<FriendEntity> friend = friendDBService.findByUsernameAndFriendName(username, friendName);
        if (!friend.isPresent()) {
            AccountEntity account = accountDBService.getByUsername(username);
            AccountEntity friendAccount = accountDBService.getByUsername(friendName);
            friendDBService.save(new FriendEntity(account, friendAccount));
            entity.acceptInvite();
        } else if (friend.get().getStatus().equals(DELETED)) {
            friend.get().setStatus(ACTIVE);
            entity.acceptInvite();
        } else {
            log.warn("[x] Failed add friend: {}, for current user: {}", friendName, username);
            throw new BadRequestException("Friend already added.");
        }
    }

    private List<AccountDto> getIngoingInvites(String username) {
            List<AccountDto> namesList = inviteDBService.getPendingInvitesByUsername(username)
                    .stream()
                    .map(entity -> mapper.map(entity.getAccount1(), AccountDto.class))
                    .collect(Collectors.toList());
            log.info("[x] Get all ingoing Invitations: {}, for current user: {}.", namesList.toString(), username);
            return namesList;
    }

    private List<AccountDto> getOutgoingInvites(String username) {
        List<AccountDto> namesList = inviteDBService.getPendingInvitesByFriendName(username)
                .stream()
                .map(entity -> mapper.map(entity.getAccount2(), AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get all outgoing Invitations: {}, for current user: {}.", namesList.toString(), username);
        return namesList;
    }

    private InviteEntity getPendingInviteByUserAndFriend(String friendName, String username) {
        return inviteDBService.getInviteByUsernameAndFriendNameInStatusPending(friendName, username);
    }

    private void createAndSaveInviteByUserAndFriend(String username, String friend) {
        AccountEntity accountEntity = accountDBService.getByUsername(username);
        AccountEntity friendEntity = accountDBService.getByUsername(friend);
        inviteDBService.save(InviteEntity.of(accountEntity, friendEntity, PENDING));
    }
}
