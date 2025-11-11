package com.Kilari.request;

import com.Kilari.domain.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtp {
    String email;
    String otp;
    private USER_ROLE role;
}
