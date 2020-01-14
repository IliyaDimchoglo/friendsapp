package com.skysoft.business.api.controller;

import com.skysoft.business.api.dto.request.AccessRequest;
import com.skysoft.business.api.dto.request.VerifyAccessRequest;
import com.skysoft.business.api.service.AccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    @PostMapping
    public ResponseEntity<Void> createAccessRequest(@Valid @RequestBody AccessRequest request) {
        accessService.createAccessRequest(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmAccess(@Valid @RequestBody VerifyAccessRequest request){
        accessService.confirmAccess(request.getEmail(), request.getConfirmationCode());
        return ResponseEntity.ok().build();
    }

}
