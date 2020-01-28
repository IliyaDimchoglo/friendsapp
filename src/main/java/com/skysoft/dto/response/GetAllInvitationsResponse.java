package com.skysoft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skysoft.dto.IngoingInvitesDto;
import com.skysoft.dto.OutgoingInvitesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
