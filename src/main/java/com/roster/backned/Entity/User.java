package com.roster.backned.Entity;

import com.roster.backned.Enumeration.Gender;
import com.roster.backned.Enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private long userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @NotNull
    private String password;
    @NotNull
    private LocalDate dob;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;
    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @PrePersist
    public void beforePersist() {
        setCreatedAt(now());
        setUpdatedAt(now());

        if (createdBy == null) {
            setCreatedBy(this);
        }

        if (updatedBy == null) {
            setUpdatedBy(this);
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        setUpdatedAt(now());

        if (updatedBy == null) {
            setUpdatedBy(this);
        }
    }

    public User(long userId, String firstName, String lastName, String email, String password, String dob, Gender gender, Role role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = LocalDate.parse(dob);
        this.gender = gender;
        this.role = role;
    }
}
