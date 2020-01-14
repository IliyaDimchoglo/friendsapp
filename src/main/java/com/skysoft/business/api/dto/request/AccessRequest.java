package com.skysoft.business.api.dto.request;


import com.skysoft.business.api.model.AccessRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public AccessRequestEntity toEntity(){
        return AccessRequestEntity.builder()
                .username(username)
                .email(email)
                .password(password)
                .confirmationCode(String.valueOf(100000 + new Random().nextInt(9000000)))
                .build();
    }
}
