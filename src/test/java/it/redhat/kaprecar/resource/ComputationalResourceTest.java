package it.redhat.kaprecar.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ComputationalResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }


    @Test
    void testFirstIteration(){
        given()
                .when().get("/calculate")
                .then()
                .statusCode(200)
                .body(is(calculateExample()));
    }

    private String calculateExample(){
        ObjectMapper mapper = new ObjectMapper();
        return  "0";

    }
}