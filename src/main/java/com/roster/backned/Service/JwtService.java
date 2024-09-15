package com.roster.backned.Service;

import com.roster.backned.Entity.User;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.UnauthorizedException;
import com.roster.backned.Repository.TokenRepository;
import com.roster.backned.Repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpire;
    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isAccessTokenValid(String accessToken, UserDetails user) {
        String email = extractEmail(accessToken);
        boolean isValidAccessToken = tokenRepository.findByAccessTokenAndLoggedOutFalse(accessToken).map(t -> !t.isLoggedOut()).orElse(false);
        return email.equals(user.getUsername()) && isTokenExpired(accessToken) && isValidAccessToken;
    }

    public boolean isValidRefreshToken(String refreshToken, User user) {
        String email = extractEmail(refreshToken);
        boolean isValidRefreshToken = tokenRepository.findByRefreshTokenAndLoggedOutFalse(refreshToken).map(t -> !t.isLoggedOut()).orElse(false);
        return email.equals(user.getUsername()) && isTokenExpired(refreshToken) && isValidRefreshToken;
    }

    public boolean isTokenExpired(String token) {
        try {
            return !extractExpiration(token).before(new Date()); // Token is not expired
        } catch (ExpiredJwtException e) {
            return true; // Token is expired
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("Token has expired: {}", e.getMessage());
            return e.getClaims();
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT error: {}", e.getMessage());
            throw new UnauthorizedException("Invalid Jwt Token");
        }
        return null;
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpire);
    }

    private String generateToken(User user, long expiredTime) {
        return Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        }
        throw new UnauthorizedException("Email not found");
    }
}