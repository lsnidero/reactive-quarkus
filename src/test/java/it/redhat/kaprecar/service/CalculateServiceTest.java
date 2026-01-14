package it.redhat.kaprecar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.KaprecarComputation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateServiceTest {

    CalculateService calc;

    @BeforeEach
    void init() {
        calc = new CalculateService();
    }

    @Test
    void areThreeIterations() throws IOException {
        KaprecarComputation expected = fromExample();
        KaprecarComputation kaprecarComputationUni = calc.calculateIterations(5432);
        assertAll("The object is the same of the expected one", () -> {
            assertEquals(expected.number(), kaprecarComputationUni.number());
            assertEquals(expected.iterations(), kaprecarComputationUni.iterations());
        });
    }

    private KaprecarComputation fromExample() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(CalculateServiceTest.class.getResource("/iteration-example.json").openStream(), KaprecarComputation.class);
    }

}