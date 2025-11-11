package br.com.mee6.paymentsjee.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    public record CreatePaymentRequest(String customerId, long amountCents, String currency) {
    }

    public record PaymentResponse(String id, String status) {
    }

    @POST
    @Counted(name = "payments_create_count", description = "Total de criacoes de pagamento")
    @Timed(name = "payments_create_time", description = "Tempo de criacao de pagamento")
    @Timeout(500)
    @Retry(maxRetries = 2, delay = 150)
    @CircuitBreaker(requestVolumeThreshold = 8, failureRatio = 0.5, delay = 2000)
    @Bulkhead(value = 20)
    public Response create(CreatePaymentRequest req) {
        if (ThreadLocalRandom.current().nextInt(10) < 3) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException ignored) {
            }
        }
        if (ThreadLocalRandom.current().nextInt(10) < 2) {
            throw new WebApplicationException("Gateway temporariamente indisponível", 503);
        }

        String id = "pay_" + System.currentTimeMillis();
        return Response.status(Response.Status.CREATED)
                .entity(new PaymentResponse(id, "NEW"))
                .build();
    }

    @GET
    @Path("/{id}")
    public Map<String, Object> get(@PathParam("id") String id) {
        return Map.of("id", id, "status", "NEW");
    }
}
