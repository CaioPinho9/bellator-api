package com.github.CaioPinho9.bellatorapi.appuser;

import com.github.CaioPinho9.bellatorapi.registration.token.ConfirmationToken;
import com.github.CaioPinho9.bellatorapi.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    /**
     * <p>Check if the user already exists</p>
     * <p>Encrypt the password using cryptPassword</p>
     * @return confirmation token, used to activate the user
     */
    public String singUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            return "email already taken";
        }

        appUser = cryptPassword(appUser);

        return createToken(appUser);
    }

    /**
     * <p>Encrypt using raw password</p>
     * <p>Changes the user password</p>
     * <p>Updates the user in the database</p>
     * @param appUser User with raw password
     * @return User with an encrypted password
     */
    public AppUser cryptPassword(AppUser appUser) {
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        return appUser;
    }

    /**
     * <p>The token is created randomly</p>
     * <p>confirmationToken receives the time it has created and expires 15 minutes later </p>
     * <p>confirmationToken is saved in the database</p>
     * @param appUser The token needs a user to confirm
     * @return The token that will be created
     */
    public String createToken(AppUser appUser) {
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    /**
     * <p>Enable the user in the database</p>
     *
     * @param email Used to find which user enable
     */
    public void enableAppUser(String email) {
        appUserRepository.enableAppUser(email);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
