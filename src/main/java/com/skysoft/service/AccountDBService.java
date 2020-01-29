package com.skysoft.service;

import com.skysoft.exception.NotFoundException;
import com.skysoft.model.AccountEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountDBService {

    Optional<AccountEntity> findByUsername(String username);

    default AccountEntity getByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new NotFoundException("Account not found"));
    }

    boolean existUsernameOrEmail(String username, String email);

    List<AccountEntity> getAllByIdIsNotIn(List<UUID> ids);

    AccountEntity save(AccountEntity account);

}
