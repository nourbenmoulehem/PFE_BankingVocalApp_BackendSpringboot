package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.exceptions.*;
import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Profile.Profile;
import com.attijari.vocalbanking.Profile.Role;
import com.attijari.vocalbanking.Client.ClientRepository;
import com.attijari.vocalbanking.Profile.ProfileRepository;
import com.attijari.vocalbanking.security.JwtService;
import com.attijari.vocalbanking.token.Token;
import com.attijari.vocalbanking.token.TokenRepository;
import com.attijari.vocalbanking.token.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ProfileRepository profileRepository;
    private final ClientRepository clientRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService; // Add EmailVerificationService dependency

    public String register(RegisterRequest request) { // AuthenticationResponse
        var client = Client.builder()
                .cin(request.getCin())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .offer(request.getOffer())
                .dateDelivrationCin(request.getDateDelivrationCin())
                .cinRecto(request.getCinRecto())
                .cinVerso(request.getCinVerso())
                .selfie(request.getSelfie())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .nationality(request.getNationality())
                .statusCivil(request.getStatusCivil())
                .nombre_enfant(request.getNombre_enfant())
                .socio_professional(request.getSocio_professional())
                .secteurActivite(request.getSecteurActivite())
                .natureActivite(request.getNatureActivite())
                .revenu(request.getRevenu())
                .codePostal(request.getCodePostal())
                .gouvernorat(request.getGouvernorat())
                //.hasOtherBank(request.isHasOtherBank())
                .agence(request.getAgence())
                .build();

        if (profileRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        if(clientRepository.findByCin(request.getCin()) != null) {
            throw new CinAlreadyExistsException(request.getCin());
        }

        // Save the Client object to the database
        Client savedClient = clientRepository.save(client);

        // Create a new Profile object
        var user = Profile.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)// Encode the password
                .client(savedClient) // Associate the Client with the Profile
                .build();

        // Save the Profile object a.k.a a user to the database
        var savedUser = profileRepository.save(user);

        // Send verification email
        emailVerificationService.sendVerificationEmail(savedUser);

        // Generate JWT tokens
        //var jwtToken = jwtService.generateToken(savedUser);
        //var refreshToken = jwtService.generateRefreshToken(savedUser);

        // Save user token
        //saveUserToken(savedUser, jwtToken);

        //return AuthenticationResponse.builder()
        //   .accessToken(jwtToken)
//                .refreshToken(refreshToken)
        // .build();

        return user.getEmail();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {


            var user = profileRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException(request.getEmail()));
          
          
            // TODO add exception USERNOTFOUND when the profile isn't associated with a client (if it has no client it means he's an admin or superadmin)
            if(user.getClient() == null) {
                throw new UserNotFoundException(request.getEmail());
            }

            Client client = user.getClient();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .clientId(client.getClientId())
                    .build();

        } catch (AuthenticationException e) {
            // Handle authentication failure
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }






    }


    private void saveUserToken(Profile user, String jwtToken) {
        var token = Token.builder()
                .profile(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    private void revokeAllUserTokens(Profile user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public Long verifyToken(String token) {
        boolean isTokenExpired = jwtService.isTokenExpired(token);
        System.out.println("isTokenExpired: " + isTokenExpired);
        String claims = jwtService.extractUsername(token);
        System.out.println("claims: " + claims);
        Profile profile = profileRepository.findByEmail(claims)
                .orElseThrow(() -> new UserNotFoundException(claims));
        if(isTokenExpired) { // throw exception if the token isn't valid
            System.out.println("Token is expired");
            throw new TokenExpiredException();
        }
        return profile.getClient().getClientId();
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        System.out.println("refreshToken: " + refreshToken);
        userEmail = jwtService.extractUsername(refreshToken);
        System.out.println("userEmail: " + userEmail);
        if (userEmail != null) {
            var user = this.profileRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
            } // TODO: Handle EXPIRED refresh token
            return null;
        }


        public void forgotPassword(String email, String cin, String phoneNumber, Date birthday) {
            var userProfile = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(email));
            Client client = clientRepository.findByCinAndPhoneNumber(cin, phoneNumber)
                    .orElseThrow(() -> new ClientNotFoundException(cin, phoneNumber, birthday));
            // Send reset password email
            System.out.println("userProfile password: " + userProfile.getPassword());
            emailVerificationService.sendResetPasswordEmail(userProfile);
//            emailVerificationService.sendResetPasswordEmail(userProfile, client);



        }

        public void newPassword(NewPasswordRequest request) {

        /* todo:
         1. Get the user profile from the token
            2. check if the token is expired
            3. check if the profile is enabled
            4. reset the password
         */

            // TODO: verify the token
            String token = request.getToken();
            System.out.println("Token: " + token);
            Jws<Claims> isTokenValid = jwtService.verifyToken(token);
            if(isTokenValid == null) { // throw exception if the token isn't valid
                System.out.println("Token is invalid");
                throw new TokenExpiredException();
            }

            // get profile by the email in the token
            Profile profile = profileRepository.findByEmail(isTokenValid.getBody().get("email").toString())
                    .orElseThrow(() -> new UserNotFoundException(isTokenValid.getBody().get("email").toString()));

            // reset the password
            profile.setPassword(passwordEncoder.encode(request.getPassword()));
            profileRepository.save(profile);
        }

    public void changePassword(ChangePasswordRequest request) {
         String currentPassword = request.getCurrentPassword();
         String newPassword = request.getNewPassword();
         String confirmPassword = request.getConfirmPassword();
         Long clientId = request.getClientId();

         Profile profile = profileRepository.findByClientId(clientId);

         System.out.println("profile: " + profile.getEmail());
         if(!passwordEncoder.matches(currentPassword, profile.getPassword())) {
             throw new InvalidPasswordException("mot de passe incorrect");
         }

        System.out.println("newwPassword" + newPassword);
        System.out.println("confirmPassword" + confirmPassword);

            if(!newPassword.equals(confirmPassword)) {
                throw new PasswordsNotMatchException("Les mots de passe ne correspondent pas");
            }

            System.out.println("before setting new password: " + profile.getPassword());
            profile.setPassword(passwordEncoder.encode(newPassword));
            profileRepository.save(profile);
            System.out.println("after setting new password: " + profile.getPassword());
    }
}


