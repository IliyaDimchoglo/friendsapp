package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.AccountDetailsDto;
import com.skysoft.business.api.dto.AccountDto;
import com.skysoft.business.api.dto.PersonalDetails;
import com.skysoft.business.api.dto.request.UpdateAccountRequest;
import com.skysoft.business.api.dto.response.GetAllAccountsResponse;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.exception.RegistrationException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.repository.AccountRepository;
import com.skysoft.business.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper mapper;

    @Value("${file.type}")
    private String contentType;

    @Override
    public ResponseEntity<GetAllAccountsResponse> getAllAccounts() {
        List<AccountDto> accounts = accountRepository.findAll()
                .stream()
                .map(account -> mapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get All accounts with size {}", accounts.size());
        return ResponseEntity.ok(GetAllAccountsResponse.of(accounts));
    }

    @Override
    public AccountEntity registerNewAccount(AccessRequestEntity request) {
        UUID accessId = request.getId();
        try {
            AccountEntity accountEntity = accountRepository.save(new AccountEntity(accessId, request));
            log.info("[x] Account registration finished. Account id {}", accountEntity.getId());
            return accountEntity;
        }catch (Exception e){
            log.warn("Account registration failed with message: {}", e.getMessage());
            throw new BadRequestException( "Account registration FAILED!");
        }
    }

    @Override
    public ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser) {
        log.info("[x] Get Account info request with access id: {}", currentUser.getUsername());
        AccountEntity accountEntity = getAccountByUsername(currentUser.getUsername());
        PersonalDetails details = mapper.map(accountEntity, PersonalDetails.class);
        return ResponseEntity.ok(new AccountDetailsDto(details));
    }

    @Override
    public ResponseEntity<Void> updateAccountInfo(UpdateAccountRequest request, CurrentUser currentUser) {
        String username = currentUser.getUsername();
        AccountEntity accountEntity = getAccountByUsername(username);
        boolean updated = accountEntity.update(request);
        if (updated) {
            accountRepository.save(accountEntity);
            log.info("[x] Successfully updated account {} for user with name: {}", accountEntity.toString(), accountEntity.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("[x] Unable to update account info.Request no data for update");
            throw new BadRequestException("Request has no data to update");
        }
    }

    @Override
    public ResponseEntity<Void> existByUsernameAndEmail(String username, String email) {
        if (accountRepository.existsByUsernameOrEmail(username, email)) {
            log.warn("[x] Account with this username {} or email {} is already exist", username, email);
            throw new RegistrationException("Account with this username or email is already exist");
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> updateAvatar(MultipartFile avatar, CurrentUser user) {
        if (requireNonNull(avatar.getContentType()).equals(contentType)) {
            AccountEntity accountEntity = getAccountByUsername(user.getUsername());
            byte[] image = avatar.getBytes();
            accountEntity.setAvatar(image);
            accountRepository.save(accountEntity);
            log.info("[x] Successful update avatar with name: {}", avatar.getOriginalFilename());
            return ResponseEntity.ok().build();
        } else {
            throw new BadRequestException("[x] The file is not valid");
        }
    }

    @Override
    public AccountEntity getAccountByUsername(String username) {
        return accountRepository.findFirstByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
