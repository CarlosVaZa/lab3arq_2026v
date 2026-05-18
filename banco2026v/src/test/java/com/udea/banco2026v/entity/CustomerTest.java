package com.udea.banco2026v.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerExhaustive() {
        // Constructores
        Customer c1 = new Customer(1L, "John", "Doe", "123456", 1000.0);
        Customer c2 = new Customer(1L, "John", "Doe", "123456", 1000.0);
        Customer empty = new Customer();

        // Getters y Setters
        empty.setId(1L);
        empty.setFirstName("John");
        empty.setLastName("Doe");
        empty.setAccountNumber("123456");
        empty.setBalance(1000.0);
        assertEquals(c1, empty);

        // toString
        assertNotNull(c1.toString());

        // Equals y HashCode - Casos Base
        assertTrue(c1.equals(c1)); // Fix: Identical comparison
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertFalse(c1.equals(null)); // Fix: Avoid assertNotEquals(obj, null)
        assertFalse(c1.equals("not a customer")); // Fix: Dissimilar types

        // Equals y HashCode - Cobertura de Ramas (Campos nulos uno por uno)
        Customer cDiff;

        cDiff = new Customer(null, "John", "Doe", "123456", 1000.0);
        assertNotEquals(c1, cDiff);
        assertNotEquals(c1.hashCode(), cDiff.hashCode());

        cDiff = new Customer(1L, null, "Doe", "123456", 1000.0);
        assertNotEquals(c1, cDiff);

        cDiff = new Customer(1L, "John", null, "123456", 1000.0);
        assertNotEquals(c1, cDiff);

        cDiff = new Customer(1L, "John", "Doe", null, 1000.0);
        assertNotEquals(c1, cDiff);

        cDiff = new Customer(1L, "John", "Doe", "123456", null);
        assertNotEquals(c1, cDiff);
        
        // Simetría de nulos
        Customer cA = new Customer(null, null, null, null, null);
        Customer cB = new Customer(null, null, null, null, null);
        assertEquals(cA, cB);
        assertEquals(cA.hashCode(), cB.hashCode());
    }
}
