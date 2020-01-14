package com.skysoft.business.api.dto.response;

import com.skysoft.business.api.dto.PersonalDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllFriendsResponse {

    private List<PersonalDetails> personalDetails;
}
