package it.redhat.kaprecar.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import it.redhat.kaprecar.domain.KaprecarComputation;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ComputationalResourceTest {
    @Test
    void testFirstIteration() throws IOException {
        given()
                .when().get("/calculation/5432")
                .then()
                .statusCode(200)
                .body("number", is(5432))
                .body("iterations", is(3));
    }


}