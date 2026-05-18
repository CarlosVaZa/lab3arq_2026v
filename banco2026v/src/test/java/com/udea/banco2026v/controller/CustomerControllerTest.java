package com.udea.banco2026v.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.banco2026v.dto.CustomerDTO;
import com.udea.banco2026v.exception.NotFoundException;
import com.udea.banco2026v.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCustomers_ShouldReturnList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCustomerById_WhenExists_ShouldReturn200() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1L);
        when(customerService.getCustomerById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getCustomerById_WhenNotFound_ShouldReturn404() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new NotFoundException("Not found"));

        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_WithValidData_ShouldReturn200() throws Exception {
        CustomerDTO dto = new CustomerDTO(null, "John", "Doe", "123456", 100.0);
        when(customerService.createCustomer(any())).thenReturn(dto);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void createCustomer_WithInvalidData_ShouldReturn400() throws Exception {
        CustomerDTO dto = new CustomerDTO(null, "", "", "123", -1.0); // Invalid

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCustomer_ShouldReturn200() throws Exception {
        CustomerDTO dto = new CustomerDTO(1L, "John", "Doe", "123456", 100.0);
        when(customerService.updateCustomer(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCustomer_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCustomerByAccountNumber_ShouldReturn200() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        when(customerService.getCustomerByAccountNumber("123456")).thenReturn(dto);

        mockMvc.perform(get("/api/customers/account/123456"))
                .andExpect(status().isOk());
    }
}
