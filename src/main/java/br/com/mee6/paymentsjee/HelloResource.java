package br.com.mee6.paymentsjee;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class HelloResource {
    @GET
    @Path("/world")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        return Response.ok("Olá Mundo de Jakarta EE 11!").build();
    }

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHelloJson() {
        return Response.ok("{\"message\": \"Olá Mundo de Jakarta EE 11!\"}").build();
    }

}