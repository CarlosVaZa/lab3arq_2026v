package com.udea.banco2026v.service;

import com.udea.banco2026v.dto.CustomerDTO;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataGeneratorService {

    private final Faker faker = new Faker();

    public List<CustomerDTO> generateCustomers(int count) {
        List<CustomerDTO> customers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CustomerDTO customer = new CustomerDTO();
            customer.setFirstName(faker.name().firstName());
            customer.setLastName(faker.name().lastName());
            customer.setAccountNumber(faker.number().digits(10));
            customer.setBalance(faker.number().randomDouble(2, 1000, 10000));
            customers.add(customer);
        }
        return customers;
    }
}
