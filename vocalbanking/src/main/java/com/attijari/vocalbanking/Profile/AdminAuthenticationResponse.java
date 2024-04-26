package com.attijari.vocalbanking.Profile;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private String email;
    private String role;
    private String id;
}
