package com.skysoft.business.api.dto.request;


import com.skysoft.business.api.model.AccessRequestEntity;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest {

    @NotNull
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Invalid email.")
    private String email;

    @NotNull
    @Length(min = 4, max = 9)
    private String username;

    @NonNull
    @Length(min = 4, max = 9)
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
