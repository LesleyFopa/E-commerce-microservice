package org.lesley.ecommerce.dtos;

import lombok.Data;
import lombok.Getter;
import org.lesley.ecommerce.entity.Address;

@Data
@Getter
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Address address;
}
