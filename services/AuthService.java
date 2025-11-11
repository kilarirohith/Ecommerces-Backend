package com.Kilari.services;

import com.Kilari.domain.USER_ROLE;

import com.Kilari.request.LoginRequest;
import com.Kilari.response.AuthResponse;
import com.Kilari.response.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse signin(LoginRequest req) throws Exception;

}
