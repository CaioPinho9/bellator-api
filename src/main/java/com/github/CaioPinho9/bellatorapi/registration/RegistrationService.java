package com.github.CaioPinho9.bellatorapi.registration;

import com.github.CaioPinho9.bellatorapi.appuser.AppUser;
import com.github.CaioPinho9.bellatorapi.appuser.AppUserRole;
import com.github.CaioPinho9.bellatorapi.appuser.AppUserService;
import com.github.CaioPinho9.bellatorapi.registration.token.ConfirmationToken;
import com.github.CaioPinho9.bellatorapi.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public ResponseEntity<String> register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email not valid");
        }
        return appUserService.singUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                        )
        );
    }

    @Transactional
    public ResponseEntity<String> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseGet(() -> null);

        if (confirmationToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("token not found");
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        return ResponseEntity.status(HttpStatus.OK).body("confirmed");
    }
}
