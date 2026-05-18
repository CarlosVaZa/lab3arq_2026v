package com.udea.banco2026v.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerDTOTest {

    @Test
    void testCustomerDTOExhaustive() {
        // Constructores
        CustomerDTO d1 = new CustomerDTO(1L, "John", "Doe", "123456", 1000.0);
        CustomerDTO d2 = new CustomerDTO(1L, "John", "Doe", "123456", 1000.0);
        CustomerDTO empty = new CustomerDTO();

        // Accessors
        empty.setId(1L);
        empty.setFirstName("John");
        empty.setLastName("Doe");
        empty.setAccountNumber("123456");
        empty.setBalance(1000.0);
        assertEquals(d1, empty);

        // toString
        assertNotNull(d1.toString());

        // Equals/HashCode Base
        assertTrue(d1.equals(d1)); // Fix: Identical comparison
        assertEquals(d1, d2);
        assertFalse(d1.equals(null)); // Fix: Avoid assertNotEquals(obj, null)
        assertFalse(d1.equals(new Object())); // Fix: Dissimilar types

        // Ramas de campos nulos
        CustomerDTO dDiff;

        dDiff = new CustomerDTO(null, "John", "Doe", "123456", 1000.0);
        assertNotEquals(d1, dDiff);

        dDiff = new CustomerDTO(1L, null, "Doe", "123456", 1000.0);
        assertNotEquals(d1, dDiff);

        dDiff = new CustomerDTO(1L, "John", null, "123456", 1000.0);
        assertNotEquals(d1, dDiff);

        dDiff = new CustomerDTO(1L, "John", "Doe", null, 1000.0);
        assertNotEquals(d1, dDiff);

        dDiff = new CustomerDTO(1L, "John", "Doe", "123456", null);
        assertNotEquals(d1, dDiff);

        // Nulos simétricos
        CustomerDTO dA = new CustomerDTO(null, null, null, null, null);
        CustomerDTO dB = new CustomerDTO(null, null, null, null, null);
        assertEquals(dA, dB);
        assertEquals(dA.hashCode(), dB.hashCode());
    }
}
