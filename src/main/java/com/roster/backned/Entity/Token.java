package com.roster.backned.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;
    private String accessToken;
    private String refreshToken;
    @Column(name = "is_logged_out")
    private boolean loggedOut;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    @CreatedDate
    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    @PrePersist
    public void beforePersist() {
        setLoggedAt(now());
    }
}
