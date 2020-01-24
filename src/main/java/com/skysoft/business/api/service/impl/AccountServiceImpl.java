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
import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.repository.AccountRepository;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.FriendDBService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.skysoft.business.api.model.FriendStatus.ACTIVE;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final FriendDBService friendDBService;
    private final ModelMapper mapper;

    @Value("${file.type}")
    private String contentType;

    @Override
    public GetAllAccountsResponse getAllAccounts(CurrentUser currentUser) {// FIXME: 23.01.20 Get all users with marked as invited and without friends
        String username = currentUser.getUsername();
        List<UUID> friendAccountsList = getFriendIdsList(username);
        List<AccountDto> accountDtoList = getAccountListWithoutFriendStatus(username, friendAccountsList);
        log.info("[x] Get All accounts without friend status: {}", accountDtoList.toString());
        return GetAllAccountsResponse.of(accountDtoList);
    }

    @Override
    @Transactional
    public AccountEntity registerNewAccount(AccessRequestEntity request) throws BadRequestException {
        try {
            AccountEntity accountEntity = accountRepository.save(request.toAccountEntity());
            log.info("[x] Account registration finished. Account id: {}.", accountEntity.getId());
            return accountEntity;
        } catch (Exception e) {
            log.warn("Account registration failed with message: {}", e.getMessage());
            throw new BadRequestException("Account registration FAILED!!!");
        }
    }

    @Override
    public AccountDetailsDto getAccountInfo(CurrentUser currentUser) {
        log.info("[x] Get Account info request with access id: {}.", currentUser.getUsername());
        AccountEntity accountEntity = getAccountByUsername(currentUser.getUsername());
        PersonalDetails details = mapper.map(accountEntity, PersonalDetails.class);
        return new AccountDetailsDto(details);
    }

    @Override
    @Transactional
    public void updateAccountInfo(UpdateAccountRequest request, CurrentUser currentUser) throws BadRequestException {
        String username = currentUser.getUsername();
        AccountEntity accountEntity = getAccountByUsername(username);
        boolean updated = accountEntity.update(request);
        if (updated) {
            log.info("[x] Successfully updated account: {}, for user with name: {}", accountEntity.toString(), accountEntity.getUsername());
        } else {
            log.warn("[x] Unable to update account info for current user: {}.", username);
            throw new BadRequestException("Bad request for update.");
        }
    }

    @Override
    public void existByUsernameAndEmail(String username, String email) throws RegistrationException {
        if (accountRepository.existsByUsernameOrEmail(username, email)) {
            log.warn("[x] Account with this username: {}, or email: {}, is already exist.", username, email);
            throw new RegistrationException("Account with this username or email is already exist.");
        }
    }

    @Async
    @Override
    @SneakyThrows
    @Transactional
    public void updateAvatar(MultipartFile avatar, CurrentUser user) throws BadRequestException {
        String username = user.getUsername();
        AccountEntity accountEntity = getAccountByUsername(username);
        if (requireNonNull(avatar.getContentType()).equals(contentType)) {
            byte[] image = avatar.getBytes();
            accountEntity.setAvatar(image);
            log.info("[x] Successful update avatar with name: {}", avatar.getOriginalFilename());
        } else {
            log.warn("[x] Failed update avatar with name: {}, for user: {}.", avatar.getOriginalFilename(), username);
            throw new BadRequestException("File is not valid.");
        }
    }

    @Override
    public AccountEntity getAccountByUsername(String username) {
        return accountRepository.findFirstByUsername(username).orElseThrow(() -> new NotFoundException("User not found."));
    }

    private UUID getFriendAccountId(FriendEntity entity, String username) {
        if (entity.getFriend().getUsername().equals(username)) {
            return entity.getAccount().getId();
        } else return entity.getFriend().getId();
    }

    private List<AccountDto> getAccountListWithoutFriendStatus(String username, List<UUID> friendAccountsList) {
        List<AccountDto> listAccounts = accountRepository.findAllByIdIsNotIn(friendAccountsList)
                .stream()
                .filter(e -> !e.getUsername().equals(username))
                .map(e -> mapper.map(e, AccountDto.class))
                .collect(Collectors.toList());
        if (listAccounts.isEmpty()) {
            listAccounts = accountRepository.findAll()
                    .stream()
                    .filter(e -> !e.getUsername().equals(username))
                    .map(e -> mapper.map(e, AccountDto.class))
                    .collect(Collectors.toList());
        }
        return listAccounts;
    }

    private List<UUID> getFriendIdsList(String username) {
        return friendDBService.getAllFriendsByAccountUsernameAndStatus(username, ACTIVE)
                .stream()
                .map(e -> getFriendAccountId(e, username))
                .collect(Collectors.toList());
    }
}
