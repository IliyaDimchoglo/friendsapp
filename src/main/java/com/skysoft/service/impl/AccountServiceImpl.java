package com.skysoft.service.impl;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.AccountDto;
import com.skysoft.dto.PersonalDetails;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.exception.BadRequestException;
import com.skysoft.exception.NotFoundException;
import com.skysoft.exception.RegistrationException;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.repository.AccountRepository;
import com.skysoft.service.AccountService;
import com.skysoft.service.FriendDBService;
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

import static com.skysoft.model.FriendStatus.ACTIVE;
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
    public GetAllAccountsResponse getAllAccounts(String currentUser) {// FIXME: 23.01.20 Get all users with marked as invited and without friends
        List<UUID> friendAccountsList = getFriendIdsList(currentUser);
        List<AccountDto> accountDtoList = getAccountListWithoutFriendStatus(currentUser, friendAccountsList);
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
    public AccountDetailsDto getAccountInfo(String currentUser) {
        log.info("[x] Get Account info request with access id: {}.", currentUser);
        AccountEntity accountEntity = getAccountByUsername(currentUser);
        PersonalDetails details = mapper.map(accountEntity, PersonalDetails.class);
        return new AccountDetailsDto(details);
    }

    @Override
    @Transactional
    public void updateAccountInfo(UpdateAccountRequest request, String currentUser) throws BadRequestException {
        AccountEntity accountEntity = getAccountByUsername(currentUser);
        boolean updated = accountEntity.update(request);
        if (updated) {
            log.info("[x] Successfully updated account: {}, for user with name: {}", accountEntity.toString(), accountEntity.getUsername());
        } else {
            log.warn("[x] Unable to update account info for current user: {}.", currentUser);
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
    public void updateAvatar(MultipartFile avatar, String user) throws BadRequestException {
        AccountEntity accountEntity = getAccountByUsername(user);
        if (requireNonNull(avatar.getContentType()).equals(contentType)) {
            byte[] image = avatar.getBytes();
            accountEntity.setAvatar(image);
            log.info("[x] Successful update avatar with name: {}", avatar.getOriginalFilename());
        } else {
            log.warn("[x] Failed update avatar with name: {}, for user: {}.", avatar.getOriginalFilename(), user);
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
