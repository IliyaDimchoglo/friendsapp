package com.skysoft.business.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessRequestEntity  extends BaseUpdatedEntity{

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column (nullable = false)
    private String password;

    private UUID confirmationCode;

    private boolean confirmed;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    public boolean isConfirmed() {
        return Objects.nonNull(account);
    }

    public AccountEntity toAccountEntity(){
        return AccountEntity.builder()
                .email(email)
                .username(username)
                .build();
    }
    // TODO: 23.01.20 toAccountEntity()
}
