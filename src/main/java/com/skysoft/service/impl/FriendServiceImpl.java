package com.skysoft.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public GetAllFriendsResponse getAllFriends(String username) {
        List<PersonalDetails> personalDetailsList = friendDBService.getAllFriendsByUsernameAndStatus(username, ACTIVE)
                .stream()
                .map(entity -> mapper.map(entity.getMyFriend(username), PersonalDetails.class))
                .collect(Collectors.toList());
        log.info("[x] Get all friends with size: {}, for user: {}.", personalDetailsList.size(), username);
        return new GetAllFriendsResponse(personalDetailsList);
    }

    @Override
    @Transactional
    public void deleteFriend(String friendName, String username) {
        FriendEntity friendEntity = friendDBService.getActiveByUsernameAndFriendName(username, friendName);
        friendEntity.delete();
        log.info("[x] Successful delete friend: {}, from friends for current user: {}.", friendName, username);
    }

}
