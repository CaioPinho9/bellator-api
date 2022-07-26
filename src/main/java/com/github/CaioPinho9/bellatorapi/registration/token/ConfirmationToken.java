package com.github.CaioPinho9.bellatorapi.registration.token;

import com.github.CaioPinho9.bellatorapi.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * <p><strong>id:</strong> database id</p>
 * <p><strong>token:</strong> token id, used to enable the user</p>
 * <p><strong>createdAt:</strong> time the token was created</p>
 * <p><strong>expiresAt:</strong> time the token expires</p>
 * <p><strong>confirmedAt:</strong> if the token was confirmed, time the token was confirmed</p>
 * <p><strong>appUser:</strong> each token has one user, each user can have multiple tokens</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "confirmation_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}
