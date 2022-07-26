package com.github.CaioPinho9.bellatorapi.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;

    /**
     * <p>Creating an user</p>
     * <p>Send confirmation email</p>
     * @param request Http Body
     * @return Http Status
     */
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    /**
     * <p>Enable an user</p>
     * @param token Confirmation token
     * @return Http Status
     */
    @PutMapping(path = "confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    /**
     * <p>Creating a new token</p>
     * <p>Send a new confirmation email</p>
     * @param request Http Body
     * @return Http Status
     */
    @PostMapping(path = "token")
    public ResponseEntity<String> newToken(@RequestBody RegistrationRequest request) {
        return registrationService.newToken(request);
    }

    /**
     * <p>Send change password email</p>
     * @param request Http Body
     * @return Http Status
     */
    @PostMapping(path = "password")
    public ResponseEntity<String> sendPasswordEmail(@RequestBody RegistrationRequest request) {
        //TODO
        return registrationService.newToken(request);
    }

    /**
     * <p>Change the password</p>
     * @param newPassword New password
     * @param request Http Body
     * @return Http Status
     */
    @PutMapping(path = "password/{newPassword}")
    public ResponseEntity<String> changePassword(@PathVariable("newPassword") String newPassword, @RequestBody RegistrationRequest request) {
        return registrationService.changePassword(newPassword, request);
    }
}
