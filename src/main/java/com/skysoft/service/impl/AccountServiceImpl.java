package com.skysoft.service.impl;

import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.AccountDto;
import com.skysoft.dto.PersonalDetails;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.Avatar;
import com.skysoft.service.AccountDBService;
import com.skysoft.service.AccountService;
import com.skysoft.service.aws.AvatarType;
import com.skysoft.service.aws.api.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.skysoft.service.aws.AvatarType.UNSUPPORTED;
import static com.skysoft.service.aws.AvatarType.getIconType;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDBService accountDBService;
    private final AmazonS3Service amazonS3Service;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public GetAllAccountsResponse getAllAccounts(String username) {
        String accountId = accountDBService.getByUsername(username).getId().toString();
        List<AccountDto> accountDtoList = accountDBService.getAllNotFriendsByAccountId(accountId)
                .stream()
                .map(e -> mapper.map(e, AccountDto.class))
                .collect(Collectors.toList());
        log.info("[x] Get All accounts without friend status: {}", accountDtoList.toString());
        return GetAllAccountsResponse.of(accountDtoList);
    }

    @Override
    @Transactional
    public AccountEntity registerNewAccount(AccessRequestEntity request) throws BadRequestException {
        try {
            request.confirm();
            AccountEntity accountEntity = accountDBService.save(request.toAccountEntity());
            log.info("[x] Account registration finished. Account id: {}.", accountEntity.getId());
            return accountEntity;
        } catch (Exception e) {
            log.warn("Account registration failed with message: {}", e.getMessage());
            throw new BadRequestException("Account registration FAILED!!!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsDto getAccountInfo(String username) {
        log.info("[x] Get Account info request with access id: {}.", username);
        AccountEntity accountEntity = accountDBService.getByUsername(username);
        PersonalDetails details = mapper.map(accountEntity, PersonalDetails.class);
        return new AccountDetailsDto(details);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameOrEmailAvailable(String username, String email) {
        if (!accountDBService.existUsernameOrEmail(username, email)) {
            log.info("Username: {}, or email: {} is available", username, email);
            return true;
        }
        log.warn("Username: {}, or email: {} is not available", username, email);
        return false;
    }

    @Override
    @Transactional
    public void updateAccountInfo(UpdateAccountRequest request, String username) throws BadRequestException {
        AccountEntity accountEntity = accountDBService.getByUsername(username);
        boolean updated = accountEntity.update(request);
        if (updated) {
            log.info("[x] Successfully updated account: {}, for user with name: {}", accountEntity.toString(), accountEntity.getUsername());
        } else {
            log.warn("[x] Unable to update account info for current user: {}.", username);
            throw new BadRequestException("Bad request for update.");
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updateAvatar(MultipartFile avatar, String username) throws BadRequestException {
        AccountEntity accountEntity = accountDBService.getByUsername(username);
        AvatarType avatarType = getIconType(avatar.getContentType());
        if(!avatarType.equals(UNSUPPORTED)){
            String avatarUrl = amazonS3Service.uploadAvatar(avatar, avatarType);
            accountEntity.addAvatar(new Avatar(avatarUrl));
            accountDBService.save(accountEntity);
            log.info("[x] Successful update avatar for user: {}", username);
        } else {
            log.warn("[x] Failed update avatar for user: {}.", username);
            throw new BadRequestException("File is not valid.");
        }
    }
}
