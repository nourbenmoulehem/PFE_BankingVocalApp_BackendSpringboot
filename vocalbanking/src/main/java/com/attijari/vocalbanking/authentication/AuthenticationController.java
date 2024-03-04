package com.attijari.vocalbanking.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping ("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String result = authenticationService.register(request);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));

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
}
