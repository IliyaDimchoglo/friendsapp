package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.PersonalDetails;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import com.skysoft.business.api.repository.FriendRepository;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.FriendService;
import com.skysoft.business.api.service.InviteDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.FriendStatus.ACTIVE;
import static com.skysoft.business.api.model.FriendStatus.DELETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final AccountService accountService;
    private final InviteDBService inviteDBService;
    private final FriendRepository friendRepository;

    @Override
    public void addAccountToFriends(FriendEntity friendEntity) {
        friendRepository.save(friendEntity);
    }

    @Override
    public ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser) {
        GetAllFriendsResponse response = new GetAllFriendsResponse();
        AccountEntity accountEntity = accountService.getAccountByUsername(currentUser.getUsername());
        List<PersonalDetails> personalDetailsList = friendRepository.findAllByUserId(accountEntity.getId())
                .stream()
                .filter(FriendEntity::isActive)
                .map(entity -> {
                    AccountEntity accountById = accountService.getAccountById(entity.getAccountEntity().getId());
                    return PersonalDetails.of(accountById);
                }).collect(Collectors.toList());
        response.setPersonalDetails(personalDetailsList);
        log.info("[x] Get all friends with size {} for user {}.", personalDetailsList.size(), currentUser.getUsername());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteFriend(DeleteFriendRequest request, CurrentUser currentUser) {
        AccountEntity currentUserEntity = accountService.getAccountByUsername(currentUser.getUsername());
        AccountEntity friendAccountEntity = accountService.getAccountByUsername(request.getUsername());
        FriendEntity friendEntity = getOneByAccountIdFriendIdAndFriendStatus(currentUserEntity.getId(), friendAccountEntity.getId(), ACTIVE);
        FriendEntity currentEntity = getOneByAccountIdFriendIdAndFriendStatus(friendAccountEntity.getId(), currentUserEntity.getId(), ACTIVE);
        try {
            friendEntity.delete();
            friendRepository.save(friendEntity);
            currentEntity.delete();
            friendRepository.save(currentEntity);
            log.info("[x] Successful delete user {} from friends for current user {}.", request.getUsername(), currentUser.getUsername());
            inviteDBService.resetInviteRequest(currentUserEntity.getId(), friendAccountEntity.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.warn("[x] Failed delete friend {} for current user {}", request.getUsername(), currentUser.getUsername());
            throw new BadRequestException("Failed delete friend for current user.");
        }
    }

    @Override
    public Optional<FriendEntity> getOptionalByAccountIdAndFriendIdAndFriendStatus(UUID accountId, UUID friendId, FriendStatus friendStatus) {
        return friendRepository.findFirstByUserIdAndAccountEntity_IdAndStatus(accountId, friendId, friendStatus);
    }
}
