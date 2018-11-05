package urkel.resources;

import com.sun.jersey.api.core.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.auth.UrkelAuthResponse;
import urkel.dao.SessionDAO;
import urkel.dao.UrkelProfileDAO;
import urkel.model.UrkelProfile;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static org.cobbzilla.util.http.HttpContentTypes.APPLICATION_JSON;
import static org.cobbzilla.wizard.resources.ResourceUtil.*;
import static urkel.ApiConstants.AUTH_ENDPOINT;

@Path(AUTH_ENDPOINT)
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Service
public class AuthResource {

    @Autowired private SessionDAO sessionDAO;
    @Autowired private UrkelProfileDAO profileDAO;

    @GET public Response list(@Context HttpContext ctx) { return ok(profileDAO.findAll()); }

    @PUT
    public Response create(@Context HttpContext ctx,
                           UrkelProfile request) {
        final UrkelProfile profile = profileDAO.findByName(request.getName());
        if (profile != null) return login(ctx, request.getName());

        final UrkelProfile newProfile = new UrkelProfile(request);
        final UrkelProfile caller = optionalUserPrincipal(ctx);
        final UrkelProfile created = profileDAO.create(newProfile);
        return ok(new UrkelAuthResponse(sessionDAO.create(created), created));
    }

    @POST @Path("/{name}")
    public Response login(@Context HttpContext ctx,
                          @PathParam("name") String name) {
        final UrkelProfile profile = profileDAO.findByName(name);
        if (profile == null) return notFound(name);
        return ok(new UrkelAuthResponse(sessionDAO.create(profile), profile));
    }

    @GET @Path("/me") public Response me(@Context HttpContext ctx) { return ok(userPrincipal(ctx)); }

}