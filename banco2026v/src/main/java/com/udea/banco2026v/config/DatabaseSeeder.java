package com.udea.banco2026v.config;

import com.udea.banco2026v.entity.Customer;
import com.udea.banco2026v.entity.Transaction;
import com.udea.banco2026v.repository.CustomerRepository;
import com.udea.banco2026v.repository.TransactionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final Faker faker = new Faker();

    public DatabaseSeeder(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            List<Customer> customers = new ArrayList<>();
            // Generamos 10 clientes por defecto usando DataFaker
            for (int i = 0; i < 10; i++) {
                Customer customer = new Customer();
                customer.setFirstName(faker.name().firstName());
                customer.setLastName(faker.name().lastName());
                customer.setAccountNumber(faker.number().digits(10));
                customer.setBalance(faker.number().randomDouble(2, 500, 9000));
                customers.add(customer);
            }
            List<Customer> savedCustomers = customerRepository.saveAll(customers);
            System.out.println(">>> [SEEDER] La base de datos estaba vacía. Se han insertado 10 clientes de prueba automáticamente.");

            // Si no hay transacciones, creamos algunas de prueba entre los clientes generados
            if (transactionRepository.count() == 0 && savedCustomers.size() >= 2) {
                List<Transaction> transactions = new ArrayList<>();
                
                // Transacción 1: Cliente 0 envía a Cliente 1
                transactions.add(createMockTransaction(
                        savedCustomers.get(0).getAccountNumber(),
                        savedCustomers.get(1).getAccountNumber(),
                        150.00
                ));

                // Transacción 2: Cliente 2 envía a Cliente 3
                transactions.add(createMockTransaction(
                        savedCustomers.get(2).getAccountNumber(),
                        savedCustomers.get(3).getAccountNumber(),
                        200.50
                ));

                // Transacción 3: Cliente 4 envía a Cliente 5
                transactions.add(createMockTransaction(
                        savedCustomers.get(4).getAccountNumber(),
                        savedCustomers.get(5).getAccountNumber(),
                        350.00
                ));

                // Transacción 4: Cliente 1 envía a Cliente 2
                transactions.add(createMockTransaction(
                        savedCustomers.get(1).getAccountNumber(),
                        savedCustomers.get(2).getAccountNumber(),
                        50.00
                ));

                transactionRepository.saveAll(transactions);
                System.out.println(">>> [SEEDER] Se han insertado 4 transacciones de prueba entre los clientes autogenerados.");
            }
        } else {
            System.out.println(">>> [SEEDER] La base de datos ya contiene registros. Omitiendo inicialización.");
        }
    }

    private Transaction createMockTransaction(String sender, String receiver, Double amount) {
        Transaction tx = new Transaction();
        tx.setSenderAccountNumber(sender);
        tx.setReceiverAccountNumber(receiver);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now().minusHours(faker.number().numberBetween(1, 48)));
        tx.setIdempotencyKey(UUID.randomUUID().toString());
        return tx;
    }
}
