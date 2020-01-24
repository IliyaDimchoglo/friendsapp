package com.skysoft.business.api.controller;

import com.skysoft.business.api.config.security.jwt.CurrentUser;
import com.skysoft.business.api.dto.AccountDetailsDto;
import com.skysoft.business.api.dto.request.AvailabilityUserRequest;
import com.skysoft.business.api.dto.request.UpdateAccountRequest;
import com.skysoft.business.api.dto.response.GetAllAccountsResponse;
import com.skysoft.business.api.service.AccountService;
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
        GetAllAccountsResponse allAccounts = accountService.getAllAccounts(currentUser);
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping("/info")
    public ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser) {
        AccountDetailsDto accountInfo = accountService.getAccountInfo(currentUser);
        return ResponseEntity.ok(accountInfo);
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateAccountInfo(@Valid @RequestBody UpdateAccountRequest updateAccountRequest, CurrentUser currentUser) {
        accountService.updateAccountInfo(updateAccountRequest, currentUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@RequestParam(name = "avatar") MultipartFile avatar, CurrentUser user) {
        accountService.updateAvatar(avatar, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> getAvailabilityUser(@Valid @RequestBody AvailabilityUserRequest request) {
        accountService.existByUsernameAndEmail(request.getUsername(), request.getEmail());
        return ResponseEntity.ok().build();
    }
}
