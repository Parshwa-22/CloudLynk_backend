package Btech.IOTcontrol.project.com.security;

import Btech.IOTcontrol.project.com.entity.AuthenticationEntity;
import Btech.IOTcontrol.project.com.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
public class AuthSecurityConfig {

    private final JWTAuthUtil jwtUtil;

    public AuthSecurityConfig(JWTAuthUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationService userService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");
                            String name = oAuth2User.getAttribute("name");

                            AuthenticationEntity user = userService.findByEmail(email);
                            if (user == null) {
                                user = userService.saveGoogleUser(email, name);
                            }

                            String token = jwtUtil.generateToken(user.getEmail());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"token\": \"" + token + "\"}");
                        })
                );

        return http.build();
    }
}
