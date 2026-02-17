package org.lesley.ecommerce.dtos;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
