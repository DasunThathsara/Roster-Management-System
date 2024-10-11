package com.roster.backned.Configuration;

import com.roster.backned.Entity.User;
import com.roster.backned.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.roster.backned.Enumeration.Gender.MALE;
import static com.roster.backned.Enumeration.Role.ADMIN;
import static com.roster.backned.Enumeration.Role.USER;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitialDataLoader {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Create and save users
            List<User> users = List.of(
                    new User(1L, "Admin", "Buddhika","admin@roster.com", "zaq123","2001-08-02",MALE, ADMIN),
                    new User(2L, "User", "Buddhika","user@roster.com","zaq123","2001-08-02",MALE,  USER)
            );

            for (User user : users) {
                if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
                    user.setCreatedAt(LocalDateTime.now());
                    user.setPassword(passwordEncoder.encode("zaq123"));
                    user.setUpdatedAt(LocalDateTime.now());
                    user.setCreatedBy(user);
                    user.setUpdatedBy(user);
                    userRepository.save(user);
                    log.info("{} user created successfully.", user.getFirstName());
                } else {
                    log.warn("{} user already exists.", user.getFirstName());
                }
            }
        };
    }
}