package com.skysoft.service;

import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {

    GetAllAccountsResponse getAllAccounts(String username);

    AccountEntity registerNewAccount(AccessRequestEntity accessRequestEntity);

    AccountDetailsDto getAccountInfo(String username);

    boolean isUsernameOrEmailAvailable(String username, String email);

    void updateAccountInfo(UpdateAccountRequest request, String username);

    void updateAvatar(MultipartFile avatar, String username);
}

