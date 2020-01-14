package com.skysoft.business.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityUserRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String email;
}
