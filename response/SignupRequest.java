// SignupRequest.java
package com.Kilari.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {

    private String email;


    private String fullName;


    private String otp;
}