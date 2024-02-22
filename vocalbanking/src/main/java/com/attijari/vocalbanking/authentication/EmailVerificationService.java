package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.model.Profile;
import com.attijari.vocalbanking.repository.ProfileRepository;
import com.attijari.vocalbanking.token.Token;
import com.attijari.vocalbanking.token.TokenRepository;
import com.attijari.vocalbanking.token.TokenType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final TokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;
    private final ProfileRepository profileRepository;

    public void sendVerificationEmail(Profile user) {
        String token = generateVerificationToken(user);
        String verificationUrl = "http://localhost:5001/api/v1/auth/verify-email?token=" + token;

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
}

