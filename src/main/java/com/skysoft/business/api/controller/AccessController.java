package com.skysoft.business.api.controller;

import com.skysoft.business.api.dto.request.AccessRequest;
import com.skysoft.business.api.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAccess(@RequestParam("id") UUID accessId,
                                              @RequestParam("confirmationCode") UUID confirmationCode){
        accessService.confirmAccess(accessId, confirmationCode);
        return ResponseEntity.ok().build();
    }

}
