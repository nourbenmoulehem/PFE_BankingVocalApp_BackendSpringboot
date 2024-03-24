package com.attijari.vocalbanking.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {
    private String password;
    private String confirmPassword;
    private String token;
}