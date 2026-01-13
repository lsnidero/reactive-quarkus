package it.redhat.kaprecar.resource;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.domain.KaprecarComputation;
import it.redhat.kaprecar.service.CacheService;
import it.redhat.kaprecar.service.CalculateService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class ComputationalResource {

    CalculateService service;
    CacheService cacheService;

    ComputationalResource(CalculateService service, CacheService cacheService) {
        this.service = service;
        this.cacheService = cacheService;
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Uni.createFrom().item("Hello from Quarkus REST");
    }


    @GET
    @Path("/calculation/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> calculate(@PathParam("number") int number) {

        return Panache.withTransaction(() ->
                service.calculateIterations(number)
                        .call(kaprecarComputation ->
                                cacheService.insertInCache(kaprecarComputation))
        ).map(computation -> Response.ok(computation).build());
    }
}
