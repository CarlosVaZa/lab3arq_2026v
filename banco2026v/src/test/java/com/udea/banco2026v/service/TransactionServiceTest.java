package com.udea.banco2026v.service;

import com.udea.banco2026v.dto.TransactionDTO;
import com.udea.banco2026v.dto.TransferRequestDTO;
import com.udea.banco2026v.entity.Customer;
import com.udea.banco2026v.entity.Transaction;
import com.udea.banco2026v.exception.NotFoundException;
import com.udea.banco2026v.mapper.TransactionMapper;
import com.udea.banco2026v.repository.CustomerRepository;
import com.udea.banco2026v.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transferMoney_WhenIdempotencyKeyExists_ShouldReturnExistingTransaction() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setIdempotencyKey("key123");
        Transaction existingTransaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();

        when(transactionRepository.findByIdempotencyKey("key123")).thenReturn(Optional.of(existingTransaction));
        when(transactionMapper.toDTO(existingTransaction)).thenReturn(dto);

        TransactionDTO result = transactionService.transferMoney(request);

        assertNotNull(result);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_WhenSuccessful_ShouldUpdateBalancesAndSaveTransaction() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setSenderAccountNumber("A1");
        request.setReceiverAccountNumber("B2");
        request.setAmount(100.0);
        request.setIdempotencyKey("key123");

        Customer sender = new Customer();
        sender.setAccountNumber("A1");
        sender.setBalance(500.0);

        Customer receiver = new Customer();
        receiver.setAccountNumber("B2");
        receiver.setBalance(200.0);

        when(transactionRepository.findByIdempotencyKey("key123")).thenReturn(Optional.empty());
        when(customerRepository.findByAccountNumber("A1")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("B2")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(transactionMapper.toDTO(any())).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.transferMoney(request);

        assertNotNull(result);
        assertEquals(400.0, sender.getBalance());
        assertEquals(300.0, receiver.getBalance());
        verify(customerRepository, times(2)).save(any(Customer.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void transferMoney_WhenSameAccount_ShouldThrowIllegalArgumentException() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setSenderAccountNumber("A1");
        request.setReceiverAccountNumber("A1");

        assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(request));
    }

    @Test
    void transferMoney_WhenInsufficientBalance_ShouldThrowIllegalStateException() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setSenderAccountNumber("A1");
        request.setReceiverAccountNumber("B2");
        request.setAmount(600.0);

        Customer sender = new Customer();
        sender.setBalance(500.0);

        when(customerRepository.findByAccountNumber("A1")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("B2")).thenReturn(Optional.of(new Customer()));

        assertThrows(IllegalStateException.class, () -> transactionService.transferMoney(request));
    }

    @Test
    void transferMoney_WhenExactBalance_ShouldSucceed() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setSenderAccountNumber("A1");
        request.setReceiverAccountNumber("B2");
        request.setAmount(500.0);

        Customer sender = new Customer();
        sender.setBalance(500.0);

        Customer receiver = new Customer();
        receiver.setBalance(0.0);

        when(customerRepository.findByAccountNumber("A1")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("B2")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any())).thenReturn(new Transaction());
        when(transactionMapper.toDTO(any())).thenReturn(new TransactionDTO());

        transactionService.transferMoney(request);

        assertEquals(0.0, sender.getBalance());
        assertEquals(500.0, receiver.getBalance());
    }

    @Test
    void getTransactionsForAccount_ShouldReturnList() {
        String acc = "A1";
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber(acc, acc))
                .thenReturn(List.of(new Transaction()));
        when(transactionMapper.toDTO(any())).thenReturn(new TransactionDTO());

        List<TransactionDTO> result = transactionService.getTransactionsForAccount(acc);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
