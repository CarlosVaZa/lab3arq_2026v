package com.udea.banco2026v.controller;

import com.udea.banco2026v.dto.TransactionDTO;
import com.udea.banco2026v.dto.TransferRequestDTO;
import com.udea.banco2026v.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.CollectionModel;

@RestController
@RequestMapping(value="/api/transactions", produces = "application/json")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) { this.transactionService = transactionService; }

    @PostMapping
    public ResponseEntity<TransactionDTO> transferMoney(
            @Valid @RequestBody TransferRequestDTO transferRequestDTO
    ) {
        TransactionDTO transaction = transactionService.transferMoney(transferRequestDTO);
        transaction.add(linkTo(methodOn(TransactionController.class).getTransactionsByAccount(transaction.getSenderAccountNumber())).withRel("sender-transactions"));
        transaction.add(linkTo(methodOn(TransactionController.class).getTransactionsByAccount(transaction.getReceiverAccountNumber())).withRel("receiver-transactions"));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<CollectionModel<TransactionDTO>> getTransactionsByAccount(@PathVariable String accountNumber) {
        List<TransactionDTO> transactions = transactionService.getTransactionsForAccount(accountNumber);
        transactions.forEach(tx -> {
            tx.add(linkTo(methodOn(TransactionController.class).getTransactionsByAccount(tx.getSenderAccountNumber())).withRel("sender-transactions"));
            tx.add(linkTo(methodOn(TransactionController.class).getTransactionsByAccount(tx.getReceiverAccountNumber())).withRel("receiver-transactions"));
        });
        CollectionModel<TransactionDTO> collectionModel = CollectionModel.of(transactions);
        collectionModel.add(linkTo(methodOn(TransactionController.class).getTransactionsByAccount(accountNumber)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }
}