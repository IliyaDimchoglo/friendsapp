package com.skysoft.business.api.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class OutgoingInvitesDto {
    List<String> namesList;
}
