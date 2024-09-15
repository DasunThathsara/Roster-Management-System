package com.roster.backned.Configuration;

import com.roster.backned.Entity.Token;
import com.roster.backned.Repository.TokenRepository;
import com.roster.backned.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or does not start with 'Bearer'");
            return;
        }
        String token = authHeader.substring(7);

        // get stored token from database
        Token storedToken = tokenRepository.findByAccessTokenAndLoggedOutFalse(token).orElse(null);
        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
            log.debug("Stored token found and updated in database");
        } else {
            log.warn("Stored token not found in database");
        }

        String email = jwtService.extractEmail(token);
        log.info("User {} signed out successfully", email);
    }
}
