package org.lesley.ecommerce.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloackConfig {



    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .realm("master")
                .serverUrl("http://localhost:9098")
                .username("admin")
                .password("1234")
                .clientId("admin-cli")
                .build();
    }
}
