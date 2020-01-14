package com.skysoft.business.api.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

import static com.skysoft.business.api.model.InviteStatus.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InviteEntity extends BaseCreatedEntity{

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private UUID friendId;

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    public static InviteEntity setAccountAndFriend(UUID accountId, UUID friendId){
        return new InviteEntity(accountId,friendId, PENDING);
    }
}
