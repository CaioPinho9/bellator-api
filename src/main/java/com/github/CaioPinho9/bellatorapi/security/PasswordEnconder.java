package com.github.CaioPinho9.bellatorapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEnconder {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @return True if both are the same
     */
    public boolean matches(String password1, String password2) {
        return bCryptPasswordEncoder().matches(password1, password2);
    }
}
