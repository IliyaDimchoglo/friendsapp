package com.skysoft.business.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
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

}
