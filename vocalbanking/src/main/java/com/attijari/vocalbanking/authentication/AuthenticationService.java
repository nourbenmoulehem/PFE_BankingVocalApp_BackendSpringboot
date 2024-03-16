package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.exceptions.CinAlreadyExistsException;
import com.attijari.vocalbanking.exceptions.ClientNotFoundException;
import com.attijari.vocalbanking.exceptions.UserAlreadyExistsException;
import com.attijari.vocalbanking.exceptions.UserNotFoundException;
import com.attijari.vocalbanking.model.Client;
import com.attijari.vocalbanking.model.Profile;
import com.attijari.vocalbanking.model.Role;
import com.attijari.vocalbanking.repository.ClientRepository;
import com.attijari.vocalbanking.repository.ProfileRepository;
import com.attijari.vocalbanking.security.JwtService;
import com.attijari.vocalbanking.token.Token;
import com.attijari.vocalbanking.token.TokenRepository;
import com.attijari.vocalbanking.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            // Handle authentication failure
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

        var user = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
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


    }


