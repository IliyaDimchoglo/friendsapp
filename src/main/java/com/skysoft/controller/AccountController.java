package com.skysoft.controller;

import com.skysoft.config.security.jwt.CurrentUser;
import com.skysoft.dto.AccountDetailsDto;
import com.skysoft.dto.request.AvailabilityUserRequest;
import com.skysoft.dto.request.UpdateAccountRequest;
import com.skysoft.dto.response.GetAllAccountsResponse;
import com.skysoft.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<GetAllAccountsResponse> getAllAccounts(CurrentUser currentUser) {
        GetAllAccountsResponse allAccounts = accountService.getAllAccounts(currentUser.getUsername());
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping("/info")
    public ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser) {
        AccountDetailsDto accountInfo = accountService.getAccountInfo(currentUser.getUsername());
        return ResponseEntity.ok(accountInfo);
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateAccountInfo(@Valid @RequestBody UpdateAccountRequest updateAccountRequest, CurrentUser currentUser) {
        accountService.updateAccountInfo(updateAccountRequest, currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@RequestParam(name = "avatar") MultipartFile avatar, CurrentUser currentUser) {
        accountService.updateAvatar(avatar, currentUser.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Boolean> userAvailability(@Valid @RequestBody AvailabilityUserRequest request) {
        boolean isAvailable = accountService.isUsernameOrEmailAvailable(request.getUsername(), request.getEmail());
        return ResponseEntity.ok(isAvailable);
    }
}
