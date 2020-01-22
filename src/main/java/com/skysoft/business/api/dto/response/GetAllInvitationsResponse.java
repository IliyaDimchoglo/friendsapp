package com.skysoft.business.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skysoft.business.api.dto.AccountDto;
import com.skysoft.business.api.dto.IngoingInvitesDto;
import com.skysoft.business.api.dto.OutgoingInvitesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetAllInvitationsResponse {

    @JsonProperty("incoming_invites")
    private IngoingInvitesDto incomingInvites;

    @JsonProperty("outgoing_invites")
    private OutgoingInvitesDto outgoingInvites;
}
