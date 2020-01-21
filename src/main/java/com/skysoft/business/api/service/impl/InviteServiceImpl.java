package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.OutgoingInvitesDto;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.FriendStatus.ACTIVE;
import static com.skysoft.business.api.model.InviteStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final AccountService accountService;
    private final InviteDBService inviteDBService;
    private final FriendService friendService;

    @Override
    public void sendInvite(AddAccountToFriendsRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        String friendName = request.getUsername();
        InviteEntity invite = inviteDBService.findInvite(username, friendName);
        if (!friendName.equals(username)) {
            if (Objects.isNull(invite) || invite.getInviteStatus().equals(REJECT)) {
                inviteDBService.save(getInviteByAccountAndFriend(username, friendName));
                log.info("[x] Successful send invite to account {}, for current user {}", friendName, username);
            }
        } else {
            log.warn("[x] Bad request account {}, for invite {}", username, friendName);
            throw new BadRequestException("Bad request for invite");
        }
    }

    @Override
    public ResponseEntity<Void> acceptInvitation(InvitationRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        String friendName = request.getUsername();
        InviteEntity inviteFriendEntity = inviteDBService.getInviteEntityByAccount_UsernameAndStatus(friendName, PENDING);
        FriendEntity friend = friendService.findByUsername(friendName);
        if (Objects.isNull(friend)) {
            setAcceptInviteStatus(inviteFriendEntity);
            addNewAccountToFriends(username, friendName);
        } else {
            friend.setStatus(ACTIVE);
            friendService.save(friend);
        }
        log.info("[x] New Friend {} successful added for account {}.", friendName, username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> rejectInvitation(InvitationRequest request, CurrentUser currentUser) {
        InviteEntity friendInviteEntity = inviteDBService.getInviteEntityByAccount_UsernameAndStatus(request.getUsername(), PENDING);
        friendInviteEntity.setInviteStatus(REJECT);
        inviteDBService.save(friendInviteEntity);
        log.info("[x] Invite {} successful reject for account {}", request.getUsername(), currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetAllInvitationsResponse> getAllInvitations(CurrentUser currentUser) {
        String username = currentUser.getUsername();
        List<String> namesList = inviteDBService.findAllInvitesByFriendUsernameAndStatus(username, PENDING)
                .stream()
                .map(InviteEntity::getAccount)
                .map(AccountEntity::getUsername)
                .collect(Collectors.toList());
        OutgoingInvitesDto outgoingInvites = getOutgoingInvites(username);
        log.info("[x] Get all invitations {}, for current user: {}.", namesList.toString(), username);
        return ResponseEntity.ok(new GetAllInvitationsResponse(namesList, outgoingInvites));
    }

    private OutgoingInvitesDto getOutgoingInvites(String username){
        List<String> namesList = inviteDBService.findAllInvitesByAccountUsernameAndStatus(username, PENDING)
                .stream()
                .map(InviteEntity::getFriend)
                .map(AccountEntity::getUsername)
                .collect(Collectors.toList());
        log.info("[x] Get all outgoing Invitations {}, for current user: {}.", namesList.toString(), username);
        return OutgoingInvitesDto.of(namesList);
    }
    private InviteEntity getInviteByAccountAndFriend(String username, String friend) {
        AccountEntity accountEntity = accountService.getAccountByUsername(username);
        AccountEntity friendEntity = accountService.getAccountByUsername(friend);
        return InviteEntity.of(accountEntity, friendEntity);
    }

    private void setAcceptInviteStatus(InviteEntity entity) {
        entity.setInviteStatus(ACCEPT);
        inviteDBService.save(entity);
    }

    private void addNewAccountToFriends(String username, String friendName) {
        AccountEntity account = accountService.getAccountByUsername(username);
        AccountEntity friendAccount = accountService.getAccountByUsername(friendName);
        friendService.save(new FriendEntity(account, friendAccount));
    }
}
