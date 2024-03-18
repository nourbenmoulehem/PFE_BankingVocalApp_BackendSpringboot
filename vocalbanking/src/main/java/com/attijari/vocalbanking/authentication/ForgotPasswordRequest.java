package com.attijari.vocalbanking.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String email;
    private String phoneNumber;
    private String cin;
    private String code;
    private Date birthday;
}
