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
        String user = currentUser.getUsername();
        GetAllAccountsResponse allAccounts = accountService.getAllAccounts(user);
        return ResponseEntity.ok(allAccounts);
    }

    @GetMapping("/info")
    public ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser) {
        String user = currentUser.getUsername();
        AccountDetailsDto accountInfo = accountService.getAccountInfo(user);
        return ResponseEntity.ok(accountInfo);
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateAccountInfo(@Valid @RequestBody UpdateAccountRequest updateAccountRequest, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        accountService.updateAccountInfo(updateAccountRequest, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@RequestParam(name = "avatar") MultipartFile avatar, CurrentUser currentUser) {
        String user = currentUser.getUsername();
        accountService.updateAvatar(avatar, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> getAvailabilityUser(@Valid @RequestBody AvailabilityUserRequest request) {
        accountService.existByUsernameAndEmail(request.getUsername(), request.getEmail());
        return ResponseEntity.ok().build();
    }
}
