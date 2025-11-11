package br.com.mee6.paymentsjee.secured;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("logout")
public class LogoutResource {

    @Context
    private HttpServletRequest request;

    @GET
    public Response logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                request.logout();
                session.invalidate();
                return Response.ok("Logout successful").build();
            } catch (ServletException e) {
                return Response.serverError().entity("Logout failed").build();
            }
        } else {
            return Response.ok("No active session").build();
        }
    }
}
