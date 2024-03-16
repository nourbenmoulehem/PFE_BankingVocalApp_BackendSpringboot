package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.model.Client;
import com.attijari.vocalbanking.model.Profile;
import com.attijari.vocalbanking.repository.ProfileRepository;
import com.attijari.vocalbanking.security.JwtService;
import com.attijari.vocalbanking.token.Token;
import com.attijari.vocalbanking.token.TokenRepository;
import com.attijari.vocalbanking.token.TokenType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final TokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;
    private final ProfileRepository profileRepository;
    private final JwtService jwtService;

    public void sendVerificationEmail(Profile user) {
        String token = generateVerificationToken(user);

//String verificationUrl = "webankAssistive://account-activation?token=" + token;
//
//            String emailContent = "<div style='text-align: center;'>"
//                    + "<h1>Enregistrement à la plateforme avec succès</h1>"
//                    + "<h3>Veuillez appuyer sur le bouton ci-dessous pour vérifier votre compte:</h3>"
//                    + "<a href='" + verificationUrl + "'>" + verificationUrl + "</a>"
//                    + "</div>";


        String verificationUrl = "http://192.168.1.7:5001/api/v1/auth/verify-email?token=" + token;
        String emailContent = "<div style='text-align: center; '>"
                + "<h1>Enregistrement à la plateforme avec succès</h1>"
                + "<h3>Veuillez appuyer sur le bouton ci-dessous pour vérifier votre compte:</h3>"
                + "<a href='" + verificationUrl + "' style='display: inline-block; background-color: #FB8500; color: black; padding: 10px 20px; border-radius: 25px; text-decoration: none; font-size: 26px; font-weight: bold; '>Vérifier</a>"
                + "</div>";
        // Send the email with HTML content
            emailSenderService.sendEmail(user.getEmail(), "Email Verification", emailContent);





    }


    private String generateVerificationToken(Profile user) {
        String token = UUID.randomUUID().toString();
        Token verificationToken = Token.builder()
                .token(token)
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .profile(user)
                .build();
        tokenRepository.save(verificationToken);
        return token;
    }

    public void verifyEmail(String token) {
        Token verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Invalid verification token"));
        Profile user = verificationToken.getProfile();
        user.setEnabled(true); // Enable the user
        profileRepository.save(user);
        tokenRepository.delete(verificationToken); // Delete the verification token
    }

    public void sendResetPasswordEmail(Profile userProfile) {
        String token = jwtService.generateResetPasswordToken(userProfile.getPassword());
        String resetPasswordUrl = "http://192.168.1.7:5001/api/v1/auth/reset-password?token=" + token;
        String emailContent = "<div style='text-align: center; '>"
                + "<h1>Réinitialisation de mot de passe</h1>"
                + "<h3>Veuillez appuyer sur le bouton ci-dessous pour réinitialiser votre mot de passe:</h3>"
                + "<a href='" + resetPasswordUrl + "' style='display: inline-block; background-color: #FB8500; color: black; padding: 10px 20px; border-radius: 25px; text-decoration: none; font-size: 26px; font-weight: bold; '>Réinitialiser</a>"
                + "</div>";
        emailSenderService.sendEmail(userProfile.getEmail(), "Réinitialisation de mot de passe", emailContent);
    }


    public void verifyResetPasswordEmail(String token) {
        if (!jwtService.verifyToken(token)) {
            throw new RuntimeException("Invalid token");
        }
    }
}

