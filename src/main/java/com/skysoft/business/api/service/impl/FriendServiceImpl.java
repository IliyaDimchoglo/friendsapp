package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.PersonalDetails;
import com.skysoft.business.api.dto.request.DeleteFriendRequest;
import com.skysoft.business.api.dto.response.GetAllFriendsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import com.skysoft.business.api.repository.FriendRepository;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.FriendStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    @Override
    public ResponseEntity<GetAllFriendsResponse> getAllFriends(CurrentUser currentUser) {
        String username = currentUser.getUsername();
        List<PersonalDetails> personalDetailsList = friendRepository.findFriendEntitiesByAccount_UsernameAndStatusOrFriend_UsernameAndStatus(username, ACTIVE, username, ACTIVE)
                .stream()
                .map(entity -> mapFriendToPersonalDetails(entity, username))
                .collect(Collectors.toList());
        log.info("[x] Get all friends with size {} for user {}.", personalDetailsList.size(), currentUser.getUsername());
        return ResponseEntity.ok(new GetAllFriendsResponse(personalDetailsList));
    }

    @Override
    public ResponseEntity<Void> deleteFriend(DeleteFriendRequest request, CurrentUser currentUser) {
        String currentUsername = currentUser.getUsername();
        String friendUsername = request.getUsername();
        FriendEntity friendEntity = getFriendEntityByAccountUsernameAndStatusAndFriendUsernameAndStatus(
                currentUsername, friendUsername, ACTIVE, friendUsername, currentUsername, ACTIVE);
        try {
            friendEntity.delete();
            friendRepository.save(friendEntity);
            log.info("[x] Successful delete user {} from friends for current user {}.", request.getUsername(), currentUser.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.warn("[x] Failed delete friend {} for current user {}", request.getUsername(), currentUser.getUsername());
            throw new BadRequestException("Failed delete friend for current user.");
        }
    }

    private PersonalDetails mapFriendToPersonalDetails(FriendEntity entity, String username) {
        if (!entity.getFriend().getUsername().equals(username)) {
            return PersonalDetails.of(entity.getFriend());
        } else {
            return PersonalDetails.of(entity.getAccount());
        }
    }

    @Override
    public Optional<FriendEntity> getOptionalFriendEntityByAccountUsernameAndStatusAndFriendUsernameAndStatus(String accountUsername, String friendName, FriendStatus status1, String friendUsername, String accountName, FriendStatus status2) {
        return friendRepository.findFriendEntityByAccount_UsernameAndFriend_UsernameAndStatusOrFriend_UsernameAndAccount_UsernameAndStatus(accountUsername, friendName, status1, friendUsername, accountName, status2);
    }

    @Override
    public void save(FriendEntity friendEntity) {
        friendRepository.save(friendEntity);
    }


    @Override
    public FriendEntity findByUsername(String username) {
        return friendRepository.findFirstByFriend_Username(username);
    }

}
