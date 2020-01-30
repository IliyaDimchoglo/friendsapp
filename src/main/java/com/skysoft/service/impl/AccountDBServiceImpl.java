package com.skysoft.service.impl;

import com.skysoft.model.AccountEntity;
import com.skysoft.repository.AccountRepository;
import com.skysoft.service.AccountDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDBServiceImpl implements AccountDBService {

    private final AccountRepository accountRepository;

    @Override
    public Optional<AccountEntity> findByUsername(String username) {
        return accountRepository.findFirstByUsername(username);
    }

    @Override
    public boolean existUsernameOrEmail(String username, String email) {
        return accountRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public List<AccountEntity> getAllNotFriendsByAccountId(String accountId) {
        return accountRepository.findAllNotFriendsByAccountId(accountId);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

}
