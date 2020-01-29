package com.skysoft.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessRequestEntity  extends BaseUpdatedEntity {

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
    public void confirm(){
        confirmed = true;
    }
}
