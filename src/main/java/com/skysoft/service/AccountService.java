package com.skysoft.service;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {

    GetAllAccountsResponse getAllAccounts(String currentUser);

    AccountEntity registerNewAccount(AccessRequestEntity accessRequestEntity);

    AccountDetailsDto getAccountInfo(String currentUser);

    void updateAccountInfo(UpdateAccountRequest request, String currentUser);

    AccountEntity getAccountByUsername(String username);

    void existByUsernameAndEmail(String firstName, String email);

    void updateAvatar(MultipartFile avatar, String user);
}

