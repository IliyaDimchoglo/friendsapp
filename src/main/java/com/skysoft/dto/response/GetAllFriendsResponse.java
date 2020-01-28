package com.skysoft.dto.response;

import com.skysoft.dto.PersonalDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllFriendsResponse {

    private List<PersonalDetails> personalDetails;
}
