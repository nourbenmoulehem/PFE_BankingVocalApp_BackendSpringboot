package com.attijari.vocalbanking.Profile;

import com.attijari.vocalbanking.authentication.AuthenticationResponse;
import com.attijari.vocalbanking.authentication.AuthenticationService;
import com.attijari.vocalbanking.exceptions.UserNotFoundException;
import com.attijari.vocalbanking.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationManager;
    private final JwtService jwtService;

    public Profile insertProfile(Profile profile) {
        System.out.println("ROLE of profile: "+profile.getRole());
        var user = Profile.builder()
                .email(profile.getEmail())
                .password(passwordEncoder.encode(profile.getPassword()))
                .role(profile.getRole())
                .isEnabled(true)
                .build();
        return profileRepository.save(user);
    }

    public ResponseEntity<AdminAuthenticationResponse> authenticate(Profile profile, HttpServletResponse response) {
        var user = profileRepository.findByEmail(profile.getEmail())
                .orElseThrow(() -> new UserNotFoundException(profile.getEmail()));
        System.out.println("user: " + user);
        // todo get a token for a session
        String jwtToken = jwtService.generateAdminSessionToken(user.getEmail(), user.getId());
        // Create a new cookie
        Cookie cookie = new Cookie("token", jwtToken);
        // Make it HttpOnly so that it's not accessible by JavaScript
        cookie.setHttpOnly(true);
        // Set the cookie to expire after 1 hour
        cookie.setMaxAge(60 * 60);
        // Optionally, set the cookie secure if you're using HTTPS
        // cookie.setSecure(true);
        // Add the cookie to the response
        response.addCookie(cookie);
        AdminAuthenticationResponse adminAuthenticationResponse = AdminAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .id(user.getId().toString())
                .build();
        return ResponseEntity.ok(adminAuthenticationResponse);
    }

    public ResponseEntity<?> verifyToken(String token) {
        Jws<Claims> claims = jwtService.verifyToken(token);
        if(claims != null) {
           System.out.println("CLAIMS: " + claims);
           long id = Long.parseLong(claims.getBody().get("id").toString());
           Optional<Profile> profile = profileRepository.findById(id);
           if(profile.isPresent()){
               Profile p = profile.get();
               System.out.println("Profile: " + p);
               System.out.println("Profile: " + profile);
               AdminAuthenticationResponse adminAuthenticationResponse = AdminAuthenticationResponse.builder()
                       .accessToken(token)
                       .email(p.getEmail())
                       .role(p.getRole().name())
                       .id(p.getId().toString())
                       .build();
               return ResponseEntity.status(200).body(adminAuthenticationResponse);
           }

        }

        return ResponseEntity.status(401).body("Invalid token");
    }
}
