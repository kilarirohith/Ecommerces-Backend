package com.Kilari.Controller;

import com.Kilari.domain.USER_ROLE;
import com.Kilari.modal.VerificationCode;
import com.Kilari.repository.UserRepository;
import com.Kilari.request.LoginOtp;
import com.Kilari.request.LoginRequest;
import com.Kilari.response.ApiResponse;
import com.Kilari.response.AuthResponse;
import com.Kilari.response.SignupRequest;
import com.Kilari.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {
         String jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("regester success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtp req) throws Exception {
        authService.sentLoginOtp(req.getEmail(),req.getRole());
        ApiResponse res = new ApiResponse();
       res.setMessage("otp sent successfully");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> createOtpHandler(@RequestBody LoginRequest req) throws Exception {
       AuthResponse authResponse= authService.signin(req);
        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent successfully");
        return ResponseEntity.ok(authResponse);
    }
}