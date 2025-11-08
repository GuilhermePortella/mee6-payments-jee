package fish.payara.secured;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Simply protected rest resource for role 'admin'.
 *
 */
@Path("admin")
@RolesAllowed("admin")
public class AdminResource {

    @Inject
    private SecurityContext securityContext;

    @GET
    public Response info() {
        String webName = null;
        if (securityContext.getCallerPrincipal() != null) {
            webName = securityContext.getCallerPrincipal().getName();
        }
        return Response
                .ok("Protected information for user:" + webName
                        + " | web user has role \"user\": " + securityContext.isCallerInRole("user")
                        + " | web user has role \"admin\": " + securityContext.isCallerInRole("admin"))
                .build();
    }
}

