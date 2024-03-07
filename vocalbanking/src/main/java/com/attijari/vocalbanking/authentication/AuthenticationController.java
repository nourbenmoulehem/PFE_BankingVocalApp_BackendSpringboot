package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.exceptions.ClientNotFoundException;
import com.attijari.vocalbanking.exceptions.UserAlreadyExistsException;
import com.attijari.vocalbanking.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
            return ResponseEntity.ok(result);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
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
    }

    @GetMapping("/reset-password")
    @ResponseBody
    public String newPassword(@RequestParam("token") String token) {
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
        return ResponseEntity.ok("Email sent successfully");
    }
}

/*
try {
        authenticationService.forgotPassword(email, cin, phoneNumber, birthday);
            return ResponseEntity.ok("Email sent successfully");
        } catch (
UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (
ClientNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found: " + e.getMessage());
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
 */