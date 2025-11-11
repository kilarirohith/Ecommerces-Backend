package com.Kilari.Controller;

import com.Kilari.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse getWelcomeMessage() {
        ApiResponse apiresponse = new ApiResponse();
       apiresponse.setMessage("Welcome to the home, let's start!");
        return apiresponse;
    }
}
