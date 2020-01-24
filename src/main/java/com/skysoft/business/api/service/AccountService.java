package com.skysoft.business.api.service;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.AccountDetailsDto;
import com.skysoft.business.api.dto.request.UpdateAccountRequest;
import com.skysoft.business.api.dto.response.GetAllAccountsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    GetAllAccountsResponse getAllAccounts(CurrentUser currentUser);

    AccountEntity registerNewAccount(AccessRequestEntity accessRequestEntity);

    AccountDetailsDto getAccountInfo(CurrentUser currentUser);

    void updateAccountInfo(UpdateAccountRequest request, CurrentUser currentUser);

    AccountEntity getAccountByUsername(String username);

    void existByUsernameAndEmail(String firstName, String email);

    void updateAvatar(MultipartFile avatar, CurrentUser user);
}

