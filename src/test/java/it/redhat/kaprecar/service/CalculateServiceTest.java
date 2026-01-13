package it.redhat.kaprecar.service;

import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.KaprecarComputation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateServiceTest {

    CalculateService calc;

    @BeforeEach
    void init() {
        calc = new CalculateService();
    }

    @Test
    void areThreeIterations() {

        Uni<KaprecarComputation> kaprecarComputationUni = calc.calculateIterations(5432);

        //assertEquals(3, kaprecarComputationUni.);
    }

}