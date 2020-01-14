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
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Void> getAvailabilityUser(@RequestBody AvailabilityUserRequest request) {
        return accountService.existByUsernameAndEmail(request.getUsername(), request.getEmail());
    }

    @GetMapping("/get_accounts")
    public ResponseEntity<GetAllAccountsResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }



    @GetMapping("/account_info")
    public ResponseEntity<AccountDetailsDto> getAccountInfo(CurrentUser currentUser) {
        return accountService.getAccountInfo(currentUser);
    }

    @PatchMapping("/update_info")
    public ResponseEntity<Void> updateAccountInfo(@Valid @RequestBody UpdateAccountRequest updateAccountRequest, CurrentUser currentUser) {
        return accountService.updateAccountInfo(updateAccountRequest, currentUser);
    }


    @PatchMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@RequestParam(name = "avatar") MultipartFile avatar, CurrentUser user) {
        return accountService.updateAvatar(avatar, user);
    }

}
