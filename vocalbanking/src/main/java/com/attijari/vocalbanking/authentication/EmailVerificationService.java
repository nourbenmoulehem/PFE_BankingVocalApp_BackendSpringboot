package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.Profile.Profile;
import com.attijari.vocalbanking.Profile.ProfileRepository;
import com.attijari.vocalbanking.exceptions.TokenExpiredException;
import com.attijari.vocalbanking.exceptions.UserNotFoundException;
import com.attijari.vocalbanking.security.JwtService;
import com.attijari.vocalbanking.token.Token;
import com.attijari.vocalbanking.token.TokenRepository;
import com.attijari.vocalbanking.token.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final TokenRepository tokenRepository;
    private final EmailSenderService emailSenderService;
    private final ProfileRepository profileRepository;
    private final CompteBancaireRepository compteBancaireRepository;
    private final JwtService jwtService;

    public void sendVerificationEmail(Profile user) {
        String token = jwtService.generateEmailVerificationToken(user.getEmail());


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


//    private String generateVerificationToken(Profile user) {
//        String token = UUID.randomUUID().toString();
//        Token verificationToken = Token.builder()
//                .token(token)
//                .tokenType(TokenType.EMAIL_VERIFICATION)
//                .profile(user)
//                .build();
//        tokenRepository.save(verificationToken);
//        return token;
//    }

//    public String generateUniqueRIB() {
//        String rib;
//        do {
//            long min = (long) Math.pow(10, 22);
//            long max = (long) Math.pow(10, 23) - 1;
//            long randomNum = ThreadLocalRandom.current().nextLong(min, max);
//            rib = String.valueOf(randomNum);
//        } while(compteBancaireRepository.existsByRIB(rib));
//        return rib;
//    }

    public void verifyEmail(String token) {
        // TODO: delete the old verification token and add a jwt verification token
//        Token verificationToken = tokenRepository.findByToken(token)
//                .orElseThrow(() -> new NoSuchElementException("Invalid verification token"));
        //        Profile user = verificationToken.getProfile();
//        tokenRepository.delete(verificationToken); // Delete the verification token

        // IN PRODUCTION: verify the token
        Jws<Claims> isTokenValid = jwtService.verifyToken(token);
        if(isTokenValid == null) { // throw exception if the token isn't valid
            System.out.println("Token is invalid");
            throw new TokenExpiredException();
        }


        Profile profile = profileRepository.findByEmail(isTokenValid.getBody().get("email").toString())
                .orElseThrow(() -> new UserNotFoundException(isTokenValid.getBody().get("email").toString()));

        System.out.println("profile: " + profile);
        profile.setEnabled(true); // Enable the user
        profileRepository.save(profile);
        // TODO: create compte bancaire with unique RIB
//        compteBancaireRepository.save(com.attijari.vocalbanking.CompteBancaire.CompteBancaire.builder()
//                .RIB(generateUniqueRIB())
//                .solde(0)
//                .profile(user)
//                .build());
        // TODO: create carte bancaire

    }

    public void sendResetPasswordEmail(Profile userProfile) {
        // sending the old encrypted password and the email in the token
        String token = jwtService.generateResetPasswordToken(userProfile.getPassword(), userProfile.getEmail());
        String resetPasswordUrl = "http://192.168.1.7:5001/api/v1/auth/reset-password?token=" + token;
        String emailContent = "<div style='text-align: center; '>"
                + "<h1>Réinitialisation de mot de passe</h1>"
                + "<h3>Veuillez appuyer sur le bouton ci-dessous pour réinitialiser votre mot de passe:</h3>"
                + "<a href='" + resetPasswordUrl + "' style='display: inline-block; background-color: #FB8500; color: black; padding: 10px 20px; border-radius: 25px; text-decoration: none; font-size: 26px; font-weight: bold; '>Réinitialiser</a>"
                + "</div>";
        emailSenderService.sendEmail(userProfile.getEmail(), "Réinitialisation de mot de passe", emailContent);
    }


    public void verifyResetPasswordEmail(String token) {
        if (jwtService.verifyToken(token) == null) {
            throw new TokenExpiredException();
        }
    }
}

