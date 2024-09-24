package com.roster.backned.Service;

import com.roster.backned.Dto.AuthenticationResponse;
import com.roster.backned.Dto.SigninDto;
import com.roster.backned.Dto.SignupDto;
import com.roster.backned.Dto.TokenRequest;
import com.roster.backned.Entity.Token;
import com.roster.backned.Entity.User;
import com.roster.backned.Enumeration.Role;
import com.roster.backned.Exception.ApiException;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.UnauthorizedException;
import com.roster.backned.Repository.TokenRepository;
import com.roster.backned.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.roster.backned.Uitils.PasswordUtils.generatePassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final ModelMapper mapper;

    @Transactional
    public AuthenticationResponse signup(SignupDto signupDto) {
        try {
            User currentUser = getCurrentUser();

            if(currentUser == null || currentUser.getRole() != Role.ADMIN) {
                log.warn("Current user is not admin");
                throw new UnauthorizedException("You are not allowed to sign up");
            }

            User user = mapper.map(signupDto, User.class);

            encodePassword(user);
            initializeUserFields(user, currentUser);
            user.setRole(determineUserRole(currentUser, user));

            user = userRepository.save(user);

            if (currentUser.getUserId() != 0) {
                log.info("User signed up successfully: {} by {}: {}", user.getUsername(), currentUser.getRole(), currentUser.getUserId());
            } else {
                log.info("User signed up successfully: {} by the user themselves", user.getUsername());
            }

            return new AuthenticationResponse("User signed up Successfully");
        } catch (Exception e) {
            log.error("Error during user signup: {}", e.getMessage());
            throw new ApiException("An error occurred during user signup");
        }
    }

    @Transactional
    public AuthenticationResponse signin(SigninDto request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));

        try {
            authenticateUser(request.getEmail(), request.getPassword());
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid email or password");
        } catch (Exception e) {
            throw new UnauthorizedException("Unable to sign in");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        log.info("{}:{} signed in Successfully", user.getRole(), user.getUsername());
        return new AuthenticationResponse(user.getUserId(), user.getFirstName(), accessToken, refreshToken, user.getRole(), "Successfully logged in");
    }

    @Transactional
    public AuthenticationResponse refreshToken(HttpServletRequest request, TokenRequest tokenRequest) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        validateAuthHeader(authHeader);

        String token = authHeader.substring(7);
        String accessEmail = jwtService.extractEmail(token);
        String refreshEmail = jwtService.extractEmail(tokenRequest.getToken());

        validateEmails(accessEmail, refreshEmail);

        User user = userRepository.findByEmail(refreshEmail).orElseThrow(() -> new NotFoundException("User not found"));

        if (jwtService.isTokenExpired(token)) {
            log.info("Access token is still valid for user: {}", refreshEmail);
            return new AuthenticationResponse(token, tokenRequest.getToken(), "Access Token is still valid");
        }

        if (jwtService.isValidRefreshToken(tokenRequest.getToken(), user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            log.info("{}:{} refreshed successfully", user.getRole(), user.getUsername());
            return new AuthenticationResponse(accessToken, refreshToken, "Token Refreshed Successfully");
        }
        throw new UnauthorizedException("Invalid Jwt Token");
    }

    private void encodePassword(User user) {
        try {
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                String password = generatePassword(6);
                user.setPassword(passwordEncoder.encode(password));
            }
        } catch (Exception e) {
            log.error("Error encoding password or sending email: {}", e.getMessage());
            throw new ApiException("An error occurred during password encoding or email sending");
        }
    }

    private void initializeUserFields(User user, User currentUser) {
        if (currentUser.getUserId() != 0) {
            user.setCreatedBy(currentUser);
            user.setUpdatedBy(currentUser);
        }
    }

    private Role determineUserRole(User currentUser, User user) {
        Role newRole = Role.USER; // Default to GUEST
        if (currentUser.getUserId() != 0 && user.getRole() != null) {
            if (Objects.requireNonNull(currentUser.getRole()) == Role.ADMIN) {
                newRole = user.getRole();
            } else {
                log.warn("Unknown role for current user: {}", currentUser.getRole());
            }
        }
        return newRole;
    }

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllAccessTokensByUser(user.getUserId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(token -> token.setLoggedOut(true));
            tokenRepository.saveAll(validTokenListByUser);
            log.info("Revoked all tokens for user {}", user.getUsername());
        }
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
        log.info("Saved new tokens for user {}", user.getUsername());
    }

    private void validateAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid JWT token in the header");
            throw new UnauthorizedException("Invalid Jwt Token");
        }
    }

    private void validateEmails(String accessEmail, String refreshEmail) {
        if (!accessEmail.equals(refreshEmail)) {
            log.warn("Access token email and refresh token email do not match");
            throw new UnauthorizedException("Invalid access/refresh token");
        }
    }

    private User getCurrentUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            if (email != null) {
                return userRepository.findByEmail(email).orElseGet(User::new);
            }
        } catch (Exception e) {
            log.error("Error fetching current user: {}", e.getMessage());
        }
        return new User();
    }
}
