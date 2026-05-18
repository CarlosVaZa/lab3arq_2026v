package com.udea.banco2026v.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.banco2026v.dto.TransactionDTO;
import com.udea.banco2026v.dto.TransferRequestDTO;
import com.udea.banco2026v.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transferMoney_WithValidData_ShouldReturn200() throws Exception {
        TransferRequestDTO request = new TransferRequestDTO("S123456", "R654321", 100.0, "36-char-uuid-v4-string-example-12345");
        when(transactionService.transferMoney(any())).thenReturn(new TransactionDTO());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void transferMoney_WithInvalidData_ShouldReturn400() throws Exception {
        TransferRequestDTO request = new TransferRequestDTO("", "", -10.0, "short");

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTransactionsByAccount_ShouldReturn200() throws Exception {
        when(transactionService.getTransactionsForAccount("123456")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions/123456"))
                .andExpect(status().isOk());
    }
}
