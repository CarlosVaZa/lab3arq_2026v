package com.udea.banco2026v.controller;

import com.udea.banco2026v.dto.CustomerDTO;
import com.udea.banco2026v.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.CollectionModel;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerFacade;

    public CustomerController(CustomerService customerFacade) {
        this.customerFacade = customerFacade;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerFacade.getAllCustomers();
        customers.forEach(customer -> {
            customer.add(linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel());
        });
        CollectionModel<CustomerDTO> collectionModel = CollectionModel.of(customers);
        collectionModel.add(linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable @Positive Long id) {
        CustomerDTO customer = customerFacade.getCustomerById(id);
        customer.add(linkTo(methodOn(CustomerController.class).getCustomerById(id)).withSelfRel());
        customer.add(linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<CustomerDTO> getCustomerByAccountNumber(
            @PathVariable String accountNumber
    ) {
        CustomerDTO customer = customerFacade.getCustomerByAccountNumber(accountNumber);
        customer.add(linkTo(methodOn(CustomerController.class).getCustomerByAccountNumber(accountNumber)).withSelfRel());
        customer.add(linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withRel("customer-details"));
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        CustomerDTO createdCustomer = customerFacade.createCustomer(customerDTO);
        createdCustomer.add(linkTo(methodOn(CustomerController.class).getCustomerById(createdCustomer.getId())).withSelfRel());
        return ResponseEntity.ok(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        CustomerDTO updatedCustomer = customerFacade.updateCustomer(id, customerDTO);
        updatedCustomer.add(linkTo(methodOn(CustomerController.class).getCustomerById(id)).withSelfRel());
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @Positive Long id){
        customerFacade.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}