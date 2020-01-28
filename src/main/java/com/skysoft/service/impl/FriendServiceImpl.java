package com.skysoft.service.impl;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.PersonalDetails;
import com.skysoft.dto.request.DeleteFriendRequest;
import com.skysoft.dto.response.GetAllFriendsResponse;
import com.skysoft.model.FriendEntity;
import com.skysoft.service.FriendDBService;
import com.skysoft.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.skysoft.model.FriendStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Override
    public GetAllFriendsResponse getAllFriends(String currentUser) {
        List<PersonalDetails> personalDetailsList = friendDBService.getAllFriendsByAccountUsernameAndStatus(currentUser, ACTIVE)
                .stream()
                .map(entity -> mapFriendToPersonalDetails(entity, currentUser))
                .collect(Collectors.toList());
        log.info("[x] Get all friends with size: {}, for user: {}.", personalDetailsList.size(), currentUser);
        return new GetAllFriendsResponse(personalDetailsList);
    }

    @Override
    @Transactional
    public void deleteFriend(DeleteFriendRequest request, String currentUser) {
        String friendUsername = request.getUsername();
        FriendEntity friendEntity = friendDBService.getFriendEntityByAccountUsernameAndStatusOrFriendUsernameAndStatus(
                currentUser, friendUsername, ACTIVE);
            friendEntity.delete();
            log.info("[x] Successful delete user: {}, from friends for current user: {}.", request.getUsername(), currentUser);
    }

    private PersonalDetails mapFriendToPersonalDetails(FriendEntity entity, String username) {
        if (!entity.getFriend().getUsername().equals(username)) {
            return mapper.map(entity.getFriend(), PersonalDetails.class);
        } else {
            return mapper.map(entity.getAccount(), PersonalDetails.class);
        }
    }

}
