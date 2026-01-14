package it.redhat.kaprecar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniSubscribe;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import it.redhat.kaprecar.domain.KaprecarComputation;
import it.redhat.kaprecar.entity.NumbersComputedEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CalculateServiceTest {

    CalculateService calc;

    @BeforeEach
    void init() {
        calc = new CalculateService();
    }

    @Test
    void areThreeIterations() throws IOException {
        KaprecarComputation expected = fromExample();
        Uni<KaprecarComputation> kaprecarComputationUni = calc.calculateIterations(5432);

        kaprecarComputationUni.subscribe().with(i -> {
            assertAll("The object is the same of the expected one" ,() ->{
                assertEquals(expected.number(), i.number());
                assertEquals(expected.iterations(), i.iterations());
            });
        });

        //FIXME: this will be correct when I resolve the immutability problem of the list for the record
        //UniAssertSubscriber<KaprecarComputation> subscriber = kaprecarComputationUni.subscribe().withSubscriber(UniAssertSubscriber.create());
        //subscriber.assertCompleted().assertItem(expected);

    }

    private KaprecarComputation fromExample() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(CalculateServiceTest.class.getResource("/iteration-example.json").openStream(), KaprecarComputation.class);
    }

}