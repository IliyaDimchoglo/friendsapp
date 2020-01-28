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
        List<PersonalDetails> personalDetailsList = friendDBService.getAllFriendsByUsernameAndStatus(currentUser, ACTIVE)
                .stream()
                .map(entity -> mapper.map(entity.getMyFriend(currentUser), PersonalDetails.class))
                .collect(Collectors.toList());
        log.info("[x] Get all friends with size: {}, for user: {}.", personalDetailsList.size(), currentUser);
        return new GetAllFriendsResponse(personalDetailsList);
    }

    @Override
    @Transactional
    public void deleteFriend(DeleteFriendRequest request, String currentUser) {
        String friendUsername = request.getUsername();
        FriendEntity friendEntity = friendDBService.getActiveByUsernameAndFriendName(// FIXME: 28.01.20 rename
                currentUser, friendUsername);
        friendEntity.delete();
        log.info("[x] Successful delete user: {}, account1 friends for current user: {}.", request.getUsername(), currentUser);
    }

}
