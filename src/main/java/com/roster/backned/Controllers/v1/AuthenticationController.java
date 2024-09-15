package com.roster.backned.Controllers.v1;

import com.roster.backned.Dto.AuthenticationResponse;
import com.roster.backned.Dto.SigninDto;
import com.roster.backned.Dto.SignupDto;
import com.roster.backned.Dto.TokenRequest;
import com.roster.backned.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@Valid @RequestBody SignupDto request) {
        return ResponseEntity.status(201).body(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@Valid @RequestBody SigninDto request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(HttpServletRequest request, @RequestBody @Valid TokenRequest tokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(request, tokenRequest));
    }
}
