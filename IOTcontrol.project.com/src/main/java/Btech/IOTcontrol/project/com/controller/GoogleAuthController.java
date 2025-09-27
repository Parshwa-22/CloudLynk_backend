package Btech.IOTcontrol.project.com.controller;


import Btech.IOTcontrol.project.com.entity.AuthenticationEntity;
import Btech.IOTcontrol.project.com.security.JWTAuthUtil;
import Btech.IOTcontrol.project.com.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    @Autowired
    private AuthenticationService userService;

    @Autowired
    private JWTAuthUtil jwtUtil;

    @GetMapping("/google-success")
    public String googleSuccess(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Save new Google user if not exist
        AuthenticationEntity user = userService.findByEmail(email);
        if (user == null) {
            user = userService.saveGoogleUser(email, name);
        }

        // Return JWT to frontend
        return jwtUtil.generateToken(user.getEmail());
    }
}

