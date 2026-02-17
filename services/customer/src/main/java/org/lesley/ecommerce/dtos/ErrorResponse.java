package org.lesley.ecommerce.dtos;

import java.util.Map;

public record ErrorResponse (
        Map<String,String> errors
) {
}
