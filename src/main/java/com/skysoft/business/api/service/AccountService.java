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

import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    ResponseEntity<GetAllAccountsResponse> getAllAccounts();

    AccountEntity registerNewAccount(AccessRequestEntity accessRequestEntity);

    ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser);

    ResponseEntity<Void> updateAccountInfo(UpdateAccountRequest request, CurrentUser currentUser);

    AccountEntity getAccountByUsername(String username);

    ResponseEntity<Void> existByUsernameAndEmail(String firstName, String email);

    ResponseEntity<Void> updateAvatar(MultipartFile avatar, CurrentUser user);

    Optional<AccountEntity> getOptionalById(UUID id);

    default AccountEntity getAccountById(UUID id) {
        return getOptionalById(id).orElseThrow(() -> new BadRequestException("Account not found"));
    }
}

