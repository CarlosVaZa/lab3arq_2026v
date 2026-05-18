package com.udea.banco2026v.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionExhaustive() {
        LocalDateTime now = LocalDateTime.now();
        Transaction t1 = new Transaction(1L, "S1", "R1", 100.0, now, "K1");
        Transaction t2 = new Transaction(1L, "S1", "R1", 100.0, now, "K1");
        Transaction empty = new Transaction();

        // Getters y Setters
        empty.setId(1L);
        empty.setSenderAccountNumber("S1");
        empty.setReceiverAccountNumber("R1");
        empty.setAmount(100.0);
        empty.setTimestamp(now);
        empty.setIdempotencyKey("K1");
        assertEquals(t1, empty);

        // toString
        assertNotNull(t1.toString());

        // Equals y HashCode - Base
        assertTrue(t1.equals(t1)); // Fix: Identical comparison
        assertEquals(t1, t2);
        assertFalse(t1.equals(null)); // Fix: Avoid assertNotEquals(obj, null)
        assertFalse(t1.equals(new Object())); // Fix: Dissimilar types

        // Equals y HashCode - Cobertura de Ramas (Campos nulos uno por uno)
        Transaction tDiff;

        tDiff = new Transaction(null, "S1", "R1", 100.0, now, "K1");
        assertNotEquals(t1, tDiff);

        tDiff = new Transaction(1L, null, "R1", 100.0, now, "K1");
        assertNotEquals(t1, tDiff);

        tDiff = new Transaction(1L, "S1", null, 100.0, now, "K1");
        assertNotEquals(t1, tDiff);

        tDiff = new Transaction(1L, "S1", "R1", null, now, "K1");
        assertNotEquals(t1, tDiff);

        tDiff = new Transaction(1L, "S1", "R1", 100.0, null, "K1");
        assertNotEquals(t1, tDiff);

        tDiff = new Transaction(1L, "S1", "R1", 100.0, now, null);
        assertNotEquals(t1, tDiff);

        // Simetría de nulos
        Transaction tA = new Transaction(null, null, null, null, null, null);
        Transaction tB = new Transaction(null, null, null, null, null, null);
        assertEquals(tA, tB);
        assertEquals(tA.hashCode(), tB.hashCode());
    }
}
