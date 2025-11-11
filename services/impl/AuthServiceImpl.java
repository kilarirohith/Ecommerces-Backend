package com.Kilari.services.impl;

import com.Kilari.config.JwtProvider;
import com.Kilari.domain.USER_ROLE;
import com.Kilari.modal.Cart;
import com.Kilari.modal.Seller;
import com.Kilari.modal.User;
import com.Kilari.modal.VerificationCode;
import com.Kilari.repository.CartRepository;
import com.Kilari.repository.SellerRepository;
import com.Kilari.repository.UserRepository;
import com.Kilari.repository.VerificationCodeRepository;
import com.Kilari.request.LoginRequest;
import com.Kilari.response.AuthResponse;
import com.Kilari.response.SignupRequest;
import com.Kilari.services.AuthService;
import com.Kilari.services.EmailService;
import com.Kilari.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private  final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private  final CustomeUserServiceImpl customeUserService;
    private final SellerRepository sellerRepository;


    public void sentLoginOtp(String email,USER_ROLE role) throws Exception {
        String SIGNING_PREFIX="signin_";
      //  String SELLER_PREFIX="seller_";
        if(email.startsWith(SIGNING_PREFIX)){
            email=email.substring(SIGNING_PREFIX.length());
            if(role.equals(USER_ROLE.ROLE_CUSTOMER)){
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null) {
                    throw new Exception("Seller not exist with provided email");
                }
            }
            else{
                User user=userRepository.findByEmail(email);
                if(user ==null) {
                    throw new Exception("user not exist with provided email");
                }
            }
        }
        VerificationCode isExist=verificationCodeRepository.findByEmail(email);
        if(isExist!=null){
            verificationCodeRepository.delete(isExist);
        }

     String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);


        String subject = "Ecommerces->KILARI ROHITH";
        String text = "Your otp login / sign up Otp Is ";
        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception{


        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Wrong otp....");
        }
       User user = userRepository.findByEmail(req.getEmail());
       if (user == null) {

           User createdUser = new User();
           createdUser.setEmail(req.getEmail());
           createdUser.setFullname(req.getFullName());
           createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
           createdUser.setPhone("7568746838457");
           createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
           user = userRepository.save(createdUser);

           Cart cart = new Cart();
           cart.setUser(user);
           cartRepository.save(cart);
       }

        List<GrantedAuthority> authorities = new ArrayList<>();
       authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

       Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signin(LoginRequest req) throws Exception {
        String  username = req.getEmail();
        String otp = req.getOtp();
        Authentication authentication = authentication(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        String rolename = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(rolename));
        return authResponse;
    }

    private Authentication authentication(String username, String otp) throws Exception {
        UserDetails userDetails = customeUserService.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)){
           username=username.substring(SELLER_PREFIX.length());
        }
        if(userDetails==null){
            throw new BadCredentialsException("invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}
