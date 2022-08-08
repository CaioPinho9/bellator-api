package com.github.CaioPinho9.bellatorapi.appuser;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.CaioPinho9.bellatorapi.utils.Token;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    /**
     * Create a new role
     *
     * @param role Role name
     * @return Http status
     */
    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.ok().body(appUserService.saveRole(role));
    }

    /**
     * Give a role to a user, giving authority to use specific resources
     *
     * @param request Email of the user and name of the role
     * @return Http status
     */
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addToUser(@RequestBody RoleToUserForm request) {
        appUserService.addRoleToUser(request.getEmail(), request.getRoleName());
        return ResponseEntity.ok().build();
    }

    /**
     * Receive a refresh token to generate a new access token
     *
     * @param request  Refresh token from header
     * @param response Http body used to log
     * @return Http status
     */
    @GetMapping("/token/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Get http header and check if there's a token
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                //Get the refresh token and decode to get user email
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = Token.decode(refreshToken);
                String email = decodedJWT.getSubject();

                //Find user utilizing email
                AppUser appUser = appUserService.findByEmail(email).orElse(null);
                if (appUser == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
                }

                //Create a new access token
                Map<String, String> tokens = Token.createTokens(appUser, request);

                //Maintain old refresh token
                tokens.put("refreshToken", refreshToken);

                //Send as response to the http body
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                //Send error to server and http body
                log.error("Error loggin in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("refresh token not found");
        }
        return null;
    }
}

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
class RoleToUserForm {
    private String email;
    private String roleName;
}
