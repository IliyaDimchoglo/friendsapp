package com.skysoft.business.api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccessRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String confirmationCode;
}
