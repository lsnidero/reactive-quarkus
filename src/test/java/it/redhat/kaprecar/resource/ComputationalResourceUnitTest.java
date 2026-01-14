package it.redhat.kaprecar.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputationalResourceUnitTest {
    @Test
    void testValidation() {
        assertAll("Are numbers valid?", () -> {
            assertTrue(ComputationalResource.isValid(5432));
            assertTrue(ComputationalResource.isValid(1123));
            assertTrue(ComputationalResource.isValid(1000));
            assertTrue(ComputationalResource.isValid(1114));
            assertFalse(ComputationalResource.isValid(1111));
            assertFalse(ComputationalResource.isValid(1));
            assertFalse(ComputationalResource.isValid(123));

        });
    }


}