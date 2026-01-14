package it.redhat.kaprecar.resource;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import it.redhat.kaprecar.service.ComputationalService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/calculation")
public class ComputationalResource {

    private static final Logger LOG = Logger.getLogger(ComputationalResource.class);

    ComputationalService service;

    ComputationalResource(ComputationalService cacheService) {
        this.service = cacheService;
    }

    @GET
    @Path("/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> calculate(@PathParam("number") int number) {
        if (!isValid(number)) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("{\"error\" : \"wrong number\"}").build());
        }

        return Panache.withTransaction(() ->
                service.calculate(number)
        ).map(computation -> Response.ok(computation).build());
    }

    @POST
    @Path("/{number}")
    public Uni<Response> queue(@PathParam("number") int number, UriInfo uriInfo) {
        if (!isValid(number)) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).build());
        }
        LOG.infof("Sending calculation request %s", number);
        return Uni.createFrom().completionStage(service.enqueue(number))
                .map(computation -> Response.created(uriInfo.getRequestUri()).build());
    }

    @GET
    @Path("/numbers")
    public Uni<Response> numbers() {
        LOG.info("Number of calculations already computed");
        return Panache.withTransaction(() ->
                service.allComputed().map(list -> Response.ok(list).build()));
    }

    static boolean isValid(int number) {
        if (number < 1000 || number > 9999) {
            return false;
        }
        LOG.infof("Validating number %s", number);
        Map<Character, Long> countMap = String.valueOf(number).chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        LOG.infof("Validating number %s", countMap);
        return countMap.values().stream().noneMatch(count -> count > 3);
    }
}
