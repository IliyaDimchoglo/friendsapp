package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.request.AddAccountToFriendsRequest;
import com.skysoft.business.api.dto.request.InvitationRequest;
import com.skysoft.business.api.dto.response.GetAllInvitationsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.FriendService;
import com.skysoft.business.api.service.InviteDBService;
import com.skysoft.business.api.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.InviteStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final FriendService friendService;
    private final AccountService accountService;
    private final InviteDBService inviteDBService;

    @Override
    public void sendInvite(AddAccountToFriendsRequest request, CurrentUser currentUser) {
        AccountEntity accountEntity = accountService.getAccountByUsername(currentUser.getUsername());
        AccountEntity friendEntity = accountService.getAccountByUsername(request.getUsername());
        boolean isAble = inviteDBService.existsInvite(accountEntity.getId(), friendEntity.getId());
        if (!isAble && !request.getUsername().equals(currentUser.getUsername())) {
            InviteEntity inviteEntity = InviteEntity.setAccountAndFriend(accountEntity.getId(), friendEntity.getId());
            try {
                inviteDBService.save(inviteEntity);
                log.info("[x] Successful send invite to account {}, for current user {}", request.getUsername(), currentUser.getUsername());
            } catch (Exception e) {
                log.warn("[x] Failed saved account with message {}", e.getMessage());
                throw new BadRequestException("Failed add account to friends.");
            }
        } else {
            log.warn("[x] Bad request account {}, for invite {}", currentUser.getUsername(), request.getUsername());
            throw new BadRequestException("Bad request for invite");
        }
    }

    @Override
    public ResponseEntity<Void> acceptInvitation(InvitationRequest request, CurrentUser currentUser) {
        AccountEntity friendEntity = accountService.getAccountByUsername(request.getUsername());
        AccountEntity currentAccountEntity = accountService.getAccountByUsername(currentUser.getUsername());
        InviteEntity friendInviteEntity= inviteDBService.getInviteEntityByAccountIdAndStatus(friendEntity.getId(), PENDING);
        if (friendInviteEntity.getAccountId().equals(friendEntity.getId())) {
            friendInviteEntity.setInviteStatus(ACCEPT);
            inviteDBService.save(friendInviteEntity);
            friendService.addAccountToFriends(new FriendEntity(currentAccountEntity.getId(), friendEntity));
            friendService.addAccountToFriends(new FriendEntity(friendEntity.getId(), currentAccountEntity));
            log.info("[x] Successful accept invitation {} for current user {}", request.getUsername(), currentUser.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("[x] Bad request: accept friend invite {}, for current user {}",friendEntity.getUsername(), currentUser.getUsername());
            throw new BadRequestException("Bad request to accept invitation");
        }
    }

    @Override
    public ResponseEntity<Void> rejectInvitation(InvitationRequest request, CurrentUser currentUser) {
        AccountEntity friendEntity = accountService.getAccountByUsername(request.getUsername());
        InviteEntity friendInviteEntity = inviteDBService.getInviteEntityByAccountIdAndStatus(friendEntity.getId(), PENDING);
        if (friendInviteEntity.getAccountId().equals(friendEntity.getId())) {
            friendInviteEntity.setInviteStatus(REJECT);
            inviteDBService.save(friendInviteEntity);
            log.info("[x] Successful reject invitation {} for current user {}", request.getUsername(), currentUser.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("[x] Bad request for reject invitation for current user: {}", currentUser.getUsername());
            throw new BadRequestException("Bad request to reject invitation");
        }
    }


    @Override
    public ResponseEntity<GetAllInvitationsResponse> getAllInvitations(CurrentUser currentUser) {
        GetAllInvitationsResponse response = new GetAllInvitationsResponse();
        AccountEntity accountEntity = accountService.getAccountByUsername(currentUser.getUsername());
        List<AccountEntity> accountEntityList = inviteDBService.findAll(accountEntity.getId(), PENDING)
                .stream()
                .map(entity -> accountService.getAccountById(entity.getFriendId()))
                .collect(Collectors.toList());
        List<String> listInvitations = accountEntityList.stream().map(AccountEntity::getUsername).collect(Collectors.toList());
        response.setFriendsNames(listInvitations);
        log.info("get all invitations size {}", listInvitations.size());
        return ResponseEntity.ok(response);
    }



}
