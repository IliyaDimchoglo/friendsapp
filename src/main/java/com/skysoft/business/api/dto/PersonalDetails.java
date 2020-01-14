package com.skysoft.business.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.skysoft.business.api.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalDetails {
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;

    public static PersonalDetails of(AccountEntity entity){
        return new PersonalDetails(
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAddress(),
                entity.getEmail(),
                entity.getPhoneNumber());
    }
}
