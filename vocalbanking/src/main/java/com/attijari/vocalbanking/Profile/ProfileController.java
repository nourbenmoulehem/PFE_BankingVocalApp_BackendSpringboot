package com.attijari.vocalbanking.Profile;


import com.attijari.vocalbanking.authentication.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // for administrators
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

     @PostMapping("/profile/register")
     public ResponseEntity<?> register(@RequestBody Profile profile) { // superadmin is going to add new admins, endpoint for superadmin
         System.out.println("profile: " + profile);
         Profile newProfile = profileService.insertProfile(profile);
         return ResponseEntity.ok(newProfile);
     }

     @PostMapping("/profile/authenticate")
     public ResponseEntity<?> authenticate(@RequestBody Profile profile, HttpServletResponse response) { // endpoint for authenticating administrators
//         ResponseEntity<AdminAuthenticationResponse> response = profileService.authenticate(profile);
         return profileService.authenticate(profile, response);
     }

     @GetMapping("/users/getUsers")
        public ResponseEntity<?> getAllProfiles() { // endpoint for getting all administrators
            return ResponseEntity.ok(profileService.getAllProfiles());
        }



        @DeleteMapping("/profile/delete/{id}")
        public ResponseEntity<?> deleteProfile(@PathVariable long id) { // endpoint for deleting an administrator
            return ResponseEntity.ok(profileService.deleteProfile(id));
        }

        @PostMapping("/profile/update")
        public ResponseEntity<?> updateProfile(@RequestBody Profile profile) { // endpoint for updating an administrator
            return ResponseEntity.ok(profileService.updateProfile(profile));
        }

     @PostMapping("/profile/verifyToken")
        public ResponseEntity<?> verifyToken(@CookieValue("token") String token) { // endpoint for verifying token
            System.out.println("token: " + token);
            return ResponseEntity.ok(profileService.verifyToken(token));
        }





    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<?> forgotPassword(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/reset-password")
    // public ResponseEntity<?> resetPassword(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/change-password")
    // public ResponseEntity<?> changePassword(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/update-profile")
    // public ResponseEntity<?> updateProfile(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/delete-profile")
    // public ResponseEntity<?> deleteProfile(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/get-profile")
    // public ResponseEntity<?> getProfile(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/get-profiles")
    // public ResponseEntity<?> getProfiles(@RequestBody Profile profile) {
    //     Profile newProfile = profileService.insertProfile(profile);
    //     return ResponseEntity.ok(newProfile);
    // }

    // @PostMapping("/get-profile-by-id")
    // public ResponseEntity<?> getProfileById(@RequestBody Profile profile) {
}
