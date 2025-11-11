package com.Kilari.response;

import com.Kilari.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
     private String jwt;
     private String message;
     private USER_ROLE role;
}
