package org.lesley.ecommerce.service;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.lesley.ecommerce.dtos.CustomerRequest;
import org.lesley.ecommerce.dtos.CustomerResponse;
import org.lesley.ecommerce.entity.Customer;
import org.lesley.ecommerce.exception.CustomerNotFoundException;
import org.lesley.ecommerce.mapper.CustomerMapper;
import org.lesley.ecommerce.repository.CustomerRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public String createCustomer( CustomerRequest customerRequest) {
        var customer = customerRepository.save(mapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    format(" Cannot update custormer:: No custumer found with the provided ID:: %s ",request.id())
                ));
        mergerCustomer(customer,request);
        customerRepository.save(customer);
    }

    private void mergerCustomer(Customer customer ,CustomerRequest request){
        if(StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existById(String id) {
        return customerRepository.existsById(id);
    }

    public CustomerResponse findById(String id) {
        return customerRepository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException("User not found with id: " + id));
    }

    public void deleteById(String id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("User not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
