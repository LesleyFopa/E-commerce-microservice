package org.lesley.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.lesley.ecommerce.dtos.*;
import org.lesley.ecommerce.service.KeycloakAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestBody LogoutRequest request) {
        return authService.logout(request.getRefreshToken())
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@RequestBody RegistrationRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);

    }
}