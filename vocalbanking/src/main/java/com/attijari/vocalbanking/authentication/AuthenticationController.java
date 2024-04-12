package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping ("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailVerificationService emailVerificationService;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
//        String result = authenticationService.register(request);
//        return ResponseEntity.ok(result);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String result = authenticationService.register(request);
            return ResponseEntity.ok("Inscription réussie. Vérifiez votre email pour valider votre compte avant de vous connecter. ✅");
        } catch (UserAlreadyExistsException e) {
            System.out.println("L'utilisateur avec cet e-mail existe déjà: ");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'utilisateur avec cet e-mail existe déjà: " + e.getMessage());
        } catch (CinAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'utilisateur avec ce CIN existe déjà.: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite. Cela peut être dû à un problème avec le serveur. Veuillez réessayer plus tard: " + e.getMessage());
        }
    }


//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bien désolé, l'utilisateur n'a pas été trouvé. Veuillez vérifier les informations fournies et réessayer.: " + e.getMessage());
        }
        catch (RuntimeException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de l'authentification: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite. Cela peut être dû à un problème avec le serveur. Veuillez réessayer plus tard: " + e.getMessage());
        }
    }

    //@GetMapping("/verify-email")
    //public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
    // emailVerificationService.verifyEmail(token);
//        return ResponseEntity.ok("Email verified successfully");
//    }

    @GetMapping("/verify-email")
    @ResponseBody
    public String verifyEmail(@RequestParam("token") String token) {
        try{
            emailVerificationService.verifyEmail(token);
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Redirecting...</title>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "        window.onload = function() {\n" +
                    "            // Replace with your deep link\n" +
                    "            var deepLink = \"webankAssistive://account-activation/" + token + "\";\n" +
                    "\n" +
                    "            window.location = deepLink;\n" +
                    "        }\n" +
                    "    </script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    Redirecting...\n" +
                    "</body>\n" +
                    "</html>";
        } catch (TokenExpiredException e) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Token Expired</title>\n" +
                    "    <style>\n" +
                    "        .expired-message {\n" +
                    "            font-size: 150px;\n" +
                    "            color: red;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"expired-message\">Le jeton a expiré</div>\n" +
                    "</body>\n" +
                    "</html>";
        } catch (IllegalStateException e) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Token Expired</title>\n" +
                    "    <style>\n" +
                    "        .expired-message {\n" +
                    "            font-size: 100px;\n" +
                    "            color: green;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"expired-message\"> Votre compte a déjà été vérifié. 👍 </div>\n" +
                    "</body>\n" +
                    "</html>";
        }

    }

    @GetMapping("/reset-password")
    @ResponseBody
    public String newPassword(@RequestParam("token") String token) {
        try {
            emailVerificationService.verifyResetPasswordEmail(token);
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Redirecting...</title>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "        window.onload = function() {\n" +
                    "            // Replace with your deep link\n" +
                    "            var deepLink = \"webankAssistive://create-new-password/" + token + "\";\n" +
                    "\n" +
                    "            window.location = deepLink;\n" +
                    "        }\n" +
                    "    </script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    Redirecting...\n" +
                    "</body>\n" +
                    "</html>";

        }
        catch (TokenExpiredException e) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Token Expired</title>\n" +
                    "    <style>\n" +
                    "        .expired-message {\n" +
                    "            font-size: 150px;\n" +
                    "            color: red;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"expired-message\">Le jeton a expiré</div>\n" +
                    "</body>\n" +
                    "</html>";
        }

    }

//    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        authenticationService.refreshToken(request, response);
//    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
            HttpServletResponse response) {

        try {
            AuthenticationResponse refreshedToken = authenticationService.refreshToken(request, response);
            return ResponseEntity.ok(refreshedToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse());
        }


    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        String cin = request.getCin();
        String phoneNumber = request.getPhoneNumber();
        Date birthday = request.getBirthday();
        authenticationService.forgotPassword(email, cin, phoneNumber, birthday);
        return ResponseEntity.ok("L'email pour réinitialiser Votre mot de passe a été envoyé avec succès");
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody NewPasswordRequest request) {
//        String token = request.getToken();
//        String newPassword = request.getNewPassword();

        try {
            authenticationService.newPassword(request);
            return ResponseEntity.ok("Mot de passe modifié avec succès");
       }
        catch (TokenExpiredException e) {
            System.out.println("Le jeton a expiré: ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LE JETON A EXPIRÉ. Veuillez réessayer en demandant un nouveau lien de réinitialisation de mot de passe.");
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bien désolé, l'utilisateur n'a pas été trouvé. Veuillez vérifier les informations fournies et réessayer.: " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite. Cela peut être dû à un problème avec le serveur. Veuillez réessayer plus tard:" + e.getMessage());
        }
    }


    // I have added this here because it belongs to the authentication flow, if the token is expired, he's lohhed out
    @PostMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestBody VerifyTokenRequest request) {
        try {
            String token = request.getAccess_token(); // getting the access_token stored in RN keychain
            authenticationService.verifyToken(token);
            return ResponseEntity.ok("Token vérifié avec succès");
        } catch (TokenExpiredException e) {
            System.out.println("Le jeton a expiré: ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LE JETON A EXPIRÉ. Veuillez réessayer en demandant un nouveau lien de réinitialisation de mot de passe.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bien désolé, l'utilisateur n'a pas été trouvé. Veuillez vérifier les informations fournies et réessayer.: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite: ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite. Cela peut être dû à un problème avec le serveur. Veuillez réessayer plus tard:" + e.getMessage());
        }
    }
}

