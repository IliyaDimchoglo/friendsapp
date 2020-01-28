package com.skysoft.service.impl;

import com.skysoft.model.FriendEntity;
import com.skysoft.model.FriendStatus;
import com.skysoft.repository.FriendRepository;
import com.skysoft.service.FriendDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.skysoft.model.FriendStatus.*;

@Service
@RequiredArgsConstructor
public class FriendDBServiceImpl implements FriendDBService {

    private final FriendRepository friendRepository;

    @Override
    public Optional<FriendEntity> findActiveByUsernameAndFriendName(String accountUsername, String friendName) {
        return friendRepository.findFriendEntityByAccount1_UsernameAndAccount2_UsernameAndStatusOrAccount2_UsernameAndAccount1_UsernameAndStatus(
                accountUsername, friendName, ACTIVE, accountUsername, friendName, ACTIVE);
    }

    @Override
    public List<FriendEntity> getAllFriendsByUsernameAndStatus(String currentUser, FriendStatus status) {
        return friendRepository.findFriendEntitiesByAccount1_UsernameAndStatusOrAccount2_UsernameAndStatus(currentUser, status, currentUser, status);
    }

    @Override
    public void save(FriendEntity friendEntity) {
        friendRepository.save(friendEntity);
    }

    @Override
    public boolean existByUsernameAndFriendNameAndStatusActive(String username, String friendName) {
        return friendRepository.existsByAccount1_UsernameAndAccount2_UsernameAndStatusOrAccount2_UsernameAndAccount1_UsernameAndStatus(
                username, friendName, ACTIVE, username, friendName, ACTIVE);
    }

    @Override
    public Optional<FriendEntity> findByUsernameAndFriendName(String username, String friendName) {
        return friendRepository.findFriendEntityByAccount1_UsernameAndAccount2_UsernameOrAccount2_UsernameAndAccount1_Username(username, friendName, username, friendName);
    }
}
