package org.lesley.ecommerce.controller;


import jakarta.validation.Valid;
import org.lesley.ecommerce.dtos.CustomerRequest;
import org.lesley.ecommerce.dtos.CustomerResponse;
import org.lesley.ecommerce.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private  final  CustomerService customerService;

  public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(
            @RequestBody @Valid CustomerRequest customerRequest){
        return ResponseEntity.ok(customerService.createCustomer(customerRequest));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
      customerService.updateCustomer(request);
      return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(){
      return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/exits/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String id){
      return ResponseEntity.ok(customerService.existById(id));
    }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> findById(@PathVariable String id) {
    return ResponseEntity.ok(customerService.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) {
    customerService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
