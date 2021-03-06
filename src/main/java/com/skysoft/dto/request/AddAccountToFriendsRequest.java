package com.skysoft.dto.request;

import com.skysoft.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAccountToFriendsRequest {

    @NotNull
    private String username;

    public String getValidUsername(String username) throws BadRequestException {
        if(!this.username.equals(username)){
            return this.username;
        }else throw new BadRequestException("Friend name is not valid");
    }

}
