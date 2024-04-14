package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.Carte.Carte;
import com.attijari.vocalbanking.Carte.CarteRepository;
import com.attijari.vocalbanking.Carte.CodeOffre;
import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientRepository;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.Profile.Profile;
import com.attijari.vocalbanking.Profile.ProfileRepository;
import com.attijari.vocalbanking.Virement.Virement;
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

    private final EmailSenderService emailSenderService;
    private final ProfileRepository profileRepository;
    private final ClientRepository clientRepository;
    private final CompteBancaireRepository compteBancaireRepository;
    private final CarteRepository carteRepository;
    private final JwtService jwtService;
    private String ip = "192.168.1.101";

    public void sendVerificationEmail(Profile user) {
        String token = jwtService.generateEmailVerificationToken(user.getEmail());


//String verificationUrl = "webankAssistive://account-activation?token=" + token;
//
//            String emailContent = "<div style='text-align: center;'>"
//                    + "<h1>Enregistrement à la plateforme avec succès</h1>"
//                    + "<h3>Veuillez appuyer sur le bouton ci-dessous pour vérifier votre compte:</h3>"
//                    + "<a href='" + verificationUrl + "'>" + verificationUrl + "</a>"
//                    + "</div>";


        String verificationUrl = "http://"+ip+":5001/api/v1/auth/verify-email?token=" + token;
       
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

//         Check if the user is already verified, we need to prevent the user from verifying the email multiple times
        if(profile.isEnabled()) {
            throw new IllegalStateException("Email already verified");
        }

        // ******* Set isEnabled to true ********
        profile.setEnabled(true); // Enable the user
        profileRepository.save(profile);

            System.out.println("Client: " + profile.getClient());
            Client client = profile.getClient();

        // TODO: create compte bancaire with unique RIB
        CompteBancaire compteBancaire = CompteBancaire.builder() // building the compte bancaire
                .solde(0)
                .client(client)
                .build();

        System.out.println("Compte bancaire: " + compteBancaire);
        System.out.println("Client from compteBancaire: " + compteBancaire.getClient());


        // set the new compte bancaire to the client
        client.setCompteBancaire(compteBancaire);

//        System.out.println("Client: " + client); //this will cause an error cause by @Data annotation so either remove @Data and replkace with @GETTER and @SETTER or use @ToString annotation and excluding compteBancaire (current solution)


        // TODO: create carte bancaire
        Carte carte = Carte.builder()
                .compteBancaire(compteBancaire)
                .code_offre(CodeOffre.CODE_007)
                .status("active")
                .build();

        // set the new carte to the compte bancaire
        compteBancaire.setCarte(carte);

        // todo save carte then compte bancaire and lastly client
        carteRepository.save(carte);
        compteBancaireRepository.save(compteBancaire);
        clientRepository.save(client);


    }

    public void sendResetPasswordEmail(Profile userProfile) {
        // sending the old encrypted password and the email in the token
        String token = jwtService.generateResetPasswordToken(userProfile.getPassword(), userProfile.getEmail());
        String resetPasswordUrl = "http://"+ip+":5001/api/v1/auth/reset-password?token=" + token;
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

    public void sendVerifyTransferEmail(Virement virement, Client client) {
        // Create the verification URL
        String verificationUrl = "http://"+ip+":5001//api/v1/operation/virement/verifier-virement/" + virement.getVir_id();

        // Create the email content
        String emailContent = "<div style='text-align: center; '>"
                + "<h1>Verification de virement</h1>"
                + "<h3>Veuillez appuyer sur le bouton ci-dessous pour vérifier votre virement:</h3>"
                + "<a href='" + verificationUrl + "' style='display: inline-block; background-color: #FB8500; color: black; padding: 10px 20px; border-radius: 25px; text-decoration: none; font-size: 26px; font-weight: bold; '>Vérifier</a>"
                + "</div>";
        // Get the email from the client's profile
        Profile profile = client.getProfile();
        if (profile != null) {
            String email = profile.getEmail();
            // use email
            emailSenderService.sendEmail(email,"Verification de virement", emailContent);
        } else {
            // handle the case where the profile is null
            throw new UserNotFoundException("");
        }

    }
}

