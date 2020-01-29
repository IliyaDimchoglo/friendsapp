package com.skysoft.service.impl;

import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.AccountDto;
import com.skysoft.dto.PersonalDetails;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.service.AccountDBService;
import com.skysoft.service.AccountService;
import com.skysoft.service.FriendDBService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.skysoft.model.FriendStatus.ACTIVE;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDBService accountDBService;
    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Value("${file.type}")
    private String contentType;

    @Override
    @Transactional(readOnly = true)
    public GetAllAccountsResponse getAllAccounts(String username) {
        List<UUID> friendAccountsList = getFriendIdsList(username);
        List<AccountDto> accountDtoList = getAccountListWithoutFriendStatus(friendAccountsList);
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
            log.info("[x] Successfully updated account1: {}, for user with name: {}", accountEntity.toString(), accountEntity.getUsername());
        } else {
            log.warn("[x] Unable account2 update account1 info for current user: {}.", username);
            throw new BadRequestException("Bad request for update.");
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updateAvatar(MultipartFile avatar, String username) throws BadRequestException {
        AccountEntity accountEntity = accountDBService.getByUsername(username);
        if (requireNonNull(avatar.getContentType()).equals(contentType)) {
            byte[] image = avatar.getBytes();
            accountEntity.setAvatar(image);
            log.info("[x] Successful update avatar with name: {}", avatar.getOriginalFilename());
        } else {
            log.warn("[x] Failed update avatar with name: {}, for user: {}.", avatar.getOriginalFilename(), username);
            throw new BadRequestException("File is not valid.");
        }
    }


    private List<AccountDto> getAccountListWithoutFriendStatus(List<UUID> friendAccountsList) {
        return accountDBService.getAllByIdIsNotIn(friendAccountsList)
                .stream()
                .map(e -> mapper.map(e, AccountDto.class))
                .collect(Collectors.toList());
    }

    private List<UUID> getFriendIdsList(String username) {
        AccountEntity myAccount = accountDBService.getByUsername(username);
        List<UUID> friendsIds = friendDBService.getAllFriendsByUsernameAndStatus(username, ACTIVE)
                .stream()
                .map(e -> e.getMyFriend(username).getId())
                .collect(Collectors.toList());
        friendsIds.add(myAccount.getId());
        return friendsIds;
    }
}
