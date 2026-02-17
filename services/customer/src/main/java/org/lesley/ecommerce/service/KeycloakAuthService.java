package org.lesley.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lesley.ecommerce.dtos.LoginRequest;
import org.lesley.ecommerce.dtos.LoginResponse;
import org.lesley.ecommerce.dtos.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthService {

    private final WebClient webClient;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.logout-uri}")
    private String logoutUri;

    @Value("${keycloak.admin-uri}")
    private String adminUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Authentifie un utilisateur via le mot de passe
     */
    public Mono<LoginResponse> login(LoginRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "password");
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> new LoginResponse(
                        (String) response.get("access_token"),
                        (String) response.get("refresh_token"),
                        String.valueOf(response.get("expires_in")),
                        (String) response.get("token_type")
                ))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Login failed: {}", ex.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Authentication failed"));
                });
    }

    /**
     * Déconnecte l'utilisateur en révoquant son refresh token
     */
    public Mono<Void> logout(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        return webClient.post()
                .uri(logoutUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Logout failed: {}", ex.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Logout failed"));
                });
    }

    /**
     * Obtient un token administrateur (client credentials)
     */
    private Mono<String> getAdminAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "client_credentials");

        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }

    /**
     * Crée un nouvel utilisateur avec le rôle 'client'
     */
    public Mono<Void> registerUser(RegistrationRequest request) {
        return getAdminAccessToken()
                .flatMap(adminToken -> {
                    // 1. Créer l'utilisateur dans Keycloak
                    Map<String, Object> userRepresentation = Map.of(
                            "username", request.getEmail(),
                            "email", request.getEmail(),
                            "firstName", request.getFirstname(),
                            "lastName", request.getLastname(),
                            "enabled", true,
                            "credentials", java.util.List.of(Map.of(
                                    "type", "password",
                                    "value", request.getPassword(),
                                    "temporary", false
                            ))
                    );

                    return webClient.post()
                            .uri(adminUri + "/users")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(userRepresentation)
                            .retrieve()
                            .toBodilessEntity()
                            .flatMap(response -> {
                                // 2. Récupérer l'ID du nouvel utilisateur depuis le header Location
                                String location = response.getHeaders().getFirst("Location");
                                if (location == null) {
                                    return Mono.error(new RuntimeException("User creation failed: no location header"));
                                }
                                String userId = location.substring(location.lastIndexOf('/') + 1);

                                // 3. Récupérer la représentation du rôle 'client'
                                return webClient.get()
                                        .uri(adminUri + "/roles/client")
                                        .header("Authorization", "Bearer " + adminToken)
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .flatMap(role -> {
                                            // 4. Assigner le rôle à l'utilisateur
                                            String roleName = (String) role.get("name");
                                            String roleId = (String) role.get("id");
                                            Map<String, Object> roleMapping = Map.of(
                                                    "id", roleId,
                                                    "name", roleName
                                            );

                                            return webClient.post()
                                                    .uri(adminUri + "/users/" + userId + "/role-mappings/realm")
                                                    .header("Authorization", "Bearer " + adminToken)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .bodyValue(java.util.List.of(roleMapping))
                                                    .retrieve()
                                                    .toBodilessEntity()
                                                    .then();
                                        });
                            });
                });
    }
}