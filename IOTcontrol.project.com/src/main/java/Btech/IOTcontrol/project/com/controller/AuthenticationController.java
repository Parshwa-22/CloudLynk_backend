package Btech.IOTcontrol.project.com.controller;



import Btech.IOTcontrol.project.com.entity.AuthenticationEntity;
import Btech.IOTcontrol.project.com.security.JWTAuthUtil;
import Btech.IOTcontrol.project.com.service.AuthenticationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService userService;

    @Autowired
    private JWTAuthUtil jwtUtil;

    // Register with username/email/password
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        AuthenticationEntity existing = userService.findByEmail(request.getEmail());
        if (existing != null) {
            return "Email already exists!";
        }
        userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        return "User registered successfully!";
    }

    // Login with email/password
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        AuthenticationEntity user = userService.findByEmail(request.getEmail());
        if (user == null) return "User not found";
        if (!userService.checkPassword(user, request.getPassword())) return "Invalid credentials";
        return jwtUtil.generateToken(user.getEmail());
    }
}

// DTOs
@Data
class RegisterRequest {
    private String username;
    private String email;
    private String password;
}

@Data
class LoginRequest {
    private String email;
    private String password;
}

