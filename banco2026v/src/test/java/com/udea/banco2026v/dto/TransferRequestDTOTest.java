package com.udea.banco2026v.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransferRequestDTOTest {

    @Test
    void testTransferRequestDTOExhaustive() {
        // Constructores
        TransferRequestDTO d1 = new TransferRequestDTO("S1", "R1", 100.0, "K123456789012345678901234567890123456");
        TransferRequestDTO d2 = new TransferRequestDTO("S1", "R1", 100.0, "K123456789012345678901234567890123456");
        TransferRequestDTO empty = new TransferRequestDTO();

        // Accessors
        empty.setSenderAccountNumber("S1");
        empty.setReceiverAccountNumber("R1");
        empty.setAmount(100.0);
        empty.setIdempotencyKey("K123456789012345678901234567890123456");
        assertEquals(d1, empty);

        // toString
        assertNotNull(d1.toString());

        // Equals/HashCode Base
        assertTrue(d1.equals(d1)); // Fix: Identical comparison
        assertEquals(d1, d2);
        assertFalse(d1.equals(null)); // Fix: Avoid assertNotEquals(obj, null)
        assertFalse(d1.equals(new Object())); // Fix: Dissimilar types

        // Ramas de campos nulos
        TransferRequestDTO dDiff;

        dDiff = new TransferRequestDTO(null, "R1", 100.0, "K123456789012345678901234567890123456");
        assertNotEquals(d1, dDiff);

        dDiff = new TransferRequestDTO("S1", null, 100.0, "K123456789012345678901234567890123456");
        assertNotEquals(d1, dDiff);

        dDiff = new TransferRequestDTO("S1", "R1", null, "K123456789012345678901234567890123456");
        assertNotEquals(d1, dDiff);

        dDiff = new TransferRequestDTO("S1", "R1", 100.0, null);
        assertNotEquals(d1, dDiff);

        // Nulos simétricos
        TransferRequestDTO dA = new TransferRequestDTO(null, null, null, null);
        TransferRequestDTO dB = new TransferRequestDTO(null, null, null, null);
        assertEquals(dA, dB);
        assertEquals(dA.hashCode(), dB.hashCode());
    }
}
