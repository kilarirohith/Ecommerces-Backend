package com.Kilari.Controller;

import com.Kilari.config.JwtProvider;
import com.Kilari.domain.AccountStatus;
import com.Kilari.exception.SellerException;
import com.Kilari.modal.Seller;
import com.Kilari.modal.SellerReport;
import com.Kilari.modal.VerificationCode;
import com.Kilari.repository.VerificationCodeRepository;
import com.Kilari.request.LoginRequest;
import com.Kilari.response.ApiResponse;
import com.Kilari.response.AuthResponse;
import com.Kilari.services.AuthService;
import com.Kilari.services.EmailService;
import com.Kilari.services.SellerReportService;
import com.Kilari.services.SellerService;
import com.Kilari.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private  final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider  jwtProvider;
    private final SellerReportService sellerReportService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

    String OTP = req.getOtp();
    String email = req.getEmail();

        req.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signin(req);
     return ResponseEntity.ok(authResponse);
 }

 @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
 }
 @PostMapping
    public  ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception , MessagingException {
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
     VerificationCode verificationCode=new VerificationCode();
     verificationCode.setEmail(seller.getEmail());
     verificationCode.setOtp(otp);
     verificationCodeRepository.save(verificationCode);
        String Subject = "Kilari kart Email Verification code";
        String text = "Welcome to Kilari Kart, Verify Your Account Using This Link";
        String Frontend_Url = "https://LocalHost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),Subject,text+Frontend_Url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);

 }

 @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
 }
 @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);
 }

 @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report, HttpStatus.OK);
 }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus status){
        List<Seller> sellers = sellerService.getAllSellers(status);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,@RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(),seller);
        return ResponseEntity.ok(updatedSeller);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
