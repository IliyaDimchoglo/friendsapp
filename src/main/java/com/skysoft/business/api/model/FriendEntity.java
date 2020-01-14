package com.skysoft.business.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendEntity extends BaseUpdatedEntity {

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

    public FriendEntity(UUID userId, AccountEntity accountEntity) {
        this.userId = userId;
        this.status = FriendStatus.ACTIVE;
        this.accountEntity = accountEntity;
    }

    public void delete(){
        status = FriendStatus.DELETED;
    }

    public boolean isActive(){
        return status.equals(FriendStatus.ACTIVE);
    }
}
