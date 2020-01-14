package com.skysoft.business.api.model;

import com.skysoft.business.api.dto.request.UpdateAccountRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique = true)
    private UUID accessId;

    @CreationTimestamp
    private Instant createdTime;

    @UpdateTimestamp
    private Instant updatedTime;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] avatar;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String address;

    public AccountEntity(UUID id, UUID accessId, AccessRequestEntity request) {
        this.id = id;
        this.accessId = accessId;
        this.email = request.getEmail();
        this.username = request.getUsername();
        this.password = request.getPassword();
    }

    public boolean update(UpdateAccountRequest request) {
        boolean updated = false;
        if (!StringUtils.isEmpty(request.getFirstName())) {
            this.firstName = request.getFirstName();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getLastName())) {
            this.lastName = request.getLastName();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getEmail())) {
            this.email = request.getEmail();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            this.phoneNumber = request.getPhoneNumber();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getAddress())) {
            this.address = request.getAddress();
            updated = true;
        }
        return updated;
    }


}
