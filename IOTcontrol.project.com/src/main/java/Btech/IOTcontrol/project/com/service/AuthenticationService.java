package Btech.IOTcontrol.project.com.service;

import Btech.IOTcontrol.project.com.entity.AuthenticationEntity;
import Btech.IOTcontrol.project.com.repository.AuthenticactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticactionRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationEntity registerUser(String username, String email, String password) {
        AuthenticationEntity user = AuthenticationEntity.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .provider("LOCAL")
                .build();
        return userRepository.save(user);
    }

    public AuthenticationEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean checkPassword(AuthenticationEntity user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public AuthenticationEntity saveGoogleUser(String email, String username) {
        AuthenticationEntity user = AuthenticationEntity.builder()
                .email(email)
                .username(username != null ? username : email)
                .provider("GOOGLE")
                .build();
        return userRepository.save(user);
    }
}

