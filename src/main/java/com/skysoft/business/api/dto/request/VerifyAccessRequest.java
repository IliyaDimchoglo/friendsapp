package com.skysoft.business.api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccessRequest {

    @NotNull
    private String email;
    @NotNull
    private String confirmationCode;
}
