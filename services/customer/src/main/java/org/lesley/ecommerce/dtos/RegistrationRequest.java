package org.lesley.ecommerce.dtos;

import lombok.Data;
import org.lesley.ecommerce.entity.Address;

@Data
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Address address;
}
