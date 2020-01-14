package com.skysoft.business.api.dto;


import lombok.Value;

@Value(staticConstructor = "of")
public class UserDto {
    String userName;
    String avatar;
}
