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

@Path("/calculation")
public class ComputationalResource {

    CacheService cacheService;

    ComputationalResource(CalculateService service, CacheService cacheService) {

        this.cacheService = cacheService;
    }

    @GET
    @Path("/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> calculate(@PathParam("number") int number) {

        return Panache.withTransaction(() ->
                cacheService.calculate(number)
        ).map(computation -> Response.ok(computation).build());
    }
}
