package com.udea.banco2026v.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionDTOTest {

    @Test
    void testTransactionDTOExhaustive() {
        LocalDateTime now = LocalDateTime.now();
        TransactionDTO d1 = new TransactionDTO(1L, "S1", "R1", 100.0, now, "K1");
        TransactionDTO d2 = new TransactionDTO(1L, "S1", "R1", 100.0, now, "K1");
        TransactionDTO empty = new TransactionDTO();

        // Getters / Setters
        empty.setId(1L);
        empty.setSenderAccountNumber("S1");
        empty.setReceiverAccountNumber("R1");
        empty.setAmount(100.0);
        empty.setTimestamp(now);
        empty.setIdempotencyKey("K1");
        assertEquals(d1, empty);

        // toString
        assertNotNull(d1.toString());

        // Equals/HashCode Base
        assertTrue(d1.equals(d1)); // Fix: Identical comparison
        assertEquals(d1, d2);
        assertFalse(d1.equals(null)); // Fix: Avoid assertNotEquals(obj, null)
        assertFalse(d1.equals(new Object())); // Fix: Dissimilar types

        // Ramas de campos nulos
        TransactionDTO dDiff;

        dDiff = new TransactionDTO(null, "S1", "R1", 100.0, now, "K1");
        assertNotEquals(d1, dDiff);

        dDiff = new TransactionDTO(1L, null, "R1", 100.0, now, "K1");
        assertNotEquals(d1, dDiff);

        dDiff = new TransactionDTO(1L, "S1", null, 100.0, now, "K1");
        assertNotEquals(d1, dDiff);

        dDiff = new TransactionDTO(1L, "S1", "R1", null, now, "K1");
        assertNotEquals(d1, dDiff);

        dDiff = new TransactionDTO(1L, "S1", "R1", 100.0, null, "K1");
        assertNotEquals(d1, dDiff);

        dDiff = new TransactionDTO(1L, "S1", "R1", 100.0, now, null);
        assertNotEquals(d1, dDiff);

        // Nulos simétricos
        TransactionDTO dA = new TransactionDTO(null, null, null, null, null, null);
        TransactionDTO dB = new TransactionDTO(null, null, null, null, null, null);
        assertEquals(dA, dB);
        assertEquals(dA.hashCode(), dB.hashCode());
    }
}
