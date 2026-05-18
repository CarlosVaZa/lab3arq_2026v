package com.udea.banco2026v.service;

import com.udea.banco2026v.dto.CustomerDTO;
import com.udea.banco2026v.entity.Customer;
import com.udea.banco2026v.exception.NotFoundException;
import com.udea.banco2026v.mapper.CustomerMapper;
import com.udea.banco2026v.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_ShouldReturnList() {
        Customer customer = new Customer();
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_WhenExists_ShouldReturnDTO() {
        Long id = 1L;
        Customer customer = new Customer();
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        CustomerDTO result = customerService.getCustomerById(id);

        assertNotNull(result);
        verify(customerRepository).findById(id);
    }

    @Test
    void getCustomerById_WhenNotExists_ShouldThrowNotFoundException() {
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void getCustomerByAccountNumber_WhenExists_ShouldReturnDTO() {
        String accountNumber = "12345";
        Customer customer = new Customer();
        CustomerDTO dto = new CustomerDTO();
        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        CustomerDTO result = customerService.getCustomerByAccountNumber(accountNumber);

        assertNotNull(result);
        verify(customerRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void getCustomerByAccountNumber_WhenNotExists_ShouldThrowNotFoundException() {
        String accountNumber = "12345";
        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getCustomerByAccountNumber(accountNumber));
    }

    @Test
    void createCustomer_WhenValid_ShouldReturnDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setAccountNumber("12345");
        Customer customer = new Customer();
        when(customerRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        when(customerMapper.toEntity(dto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(dto);

        CustomerDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomer_WhenDuplicateAccount_ShouldThrowIllegalArgumentException() {
        CustomerDTO dto = new CustomerDTO();
        dto.setAccountNumber("12345");
        when(customerRepository.findByAccountNumber("12345")).thenReturn(Optional.of(new Customer()));

        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void updateCustomer_WhenValid_ShouldReturnDTO() {
        Long id = 1L;
        CustomerDTO dto = new CustomerDTO();
        dto.setAccountNumber("54321");
        Customer customer = new Customer();
        customer.setId(id);
        
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByAccountNumber("54321")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(any(Customer.class))).thenReturn(dto);

        CustomerDTO result = customerService.updateCustomer(id, dto);

        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_WhenValid_ShouldDelete() {
        Long id = 1L;
        Customer customer = new Customer();
        customer.setBalance(0.0);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(id);

        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_WhenBalancePositive_ShouldThrowIllegalStateException() {
        Long id = 1L;
        Customer customer = new Customer();
        customer.setBalance(100.0);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        assertThrows(IllegalStateException.class, () -> customerService.deleteCustomer(id));
    }
}
