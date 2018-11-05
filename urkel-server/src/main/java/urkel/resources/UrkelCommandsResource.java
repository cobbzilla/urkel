package urkel.resources;

import com.sun.jersey.api.core.HttpContext;
import org.cobbzilla.util.collection.NameAndValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.dao.UrkelCommandDriverDAO;
import urkel.model.UrkelCommand;
import urkel.model.UrkelCommandDriver;
import urkel.model.UrkelProfile;
import urkel.service.UrkelCommandService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static org.cobbzilla.util.http.HttpContentTypes.APPLICATION_JSON;
import static org.cobbzilla.wizard.resources.ResourceUtil.*;
import static urkel.ApiConstants.COMMANDS_ENDPOINT;

@Path(COMMANDS_ENDPOINT)
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Service
public class UrkelCommandsResource {

    @Autowired private UrkelCommandDriverDAO commandDriverDAO;
    @Autowired private UrkelCommandService commandService;

    @GET @Path("/{name}")
    public Response find(@Context HttpContext ctx,
                         @PathParam("name") String name) {
        final UrkelProfile profile = userPrincipal(ctx);
        final UrkelCommandDriver template = commandDriverDAO.findByName(name);
        return template == null ? notFound(name) : ok(template);
    }

    @PUT
    public Response createOrUpdate(@Context HttpContext ctx,
                                   UrkelCommandDriver request) {

        final UrkelProfile profile = userPrincipal(ctx);
        // if (!profile.isAdmin()) return forbidden();

        UrkelCommandDriver template = commandDriverDAO.findByName(request.getName());
        if (template != null) {
            template.update(request);
            template = commandDriverDAO.update(template);
        } else {
            template = commandDriverDAO.create(new UrkelCommandDriver(request));
        }
        return ok(template);
    }

    @POST @Path("/{name}")
    public Response invoke(@Context HttpContext ctx,
                           @PathParam("name") String name,
                           NameAndValue[] params) {

        final UrkelProfile profile = userPrincipal(ctx);
        final UrkelCommandDriver template = commandDriverDAO.findByName(name);
        if (template == null /*|| (template.isAdmin() && !profile.isAdmin())*/) return notFound(name);

        return ok(commandService.start(new UrkelCommand(profile, template, params)));
    }

    @GET @Path("/{name}/status/{token}")
    public Response status(@Context HttpContext ctx,
                           @PathParam("name") String name,
                           @PathParam("token") String token) {

        final UrkelProfile profile = userPrincipal(ctx);
        final UrkelCommandDriver template = commandDriverDAO.findByName(name);
        if (template == null /*|| (template.isAdmin() && !profile.isAdmin())*/) return notFound(name);

        return ok(commandService.status(token));
    }

    @DELETE @Path("/{name}")
    public Response delete(@Context HttpContext ctx,
                           @PathParam("name") String name) {
        final UrkelProfile profile = userPrincipal(ctx);
        // if (!profile.isAdmin()) return forbidden();
        final UrkelCommandDriver template = commandDriverDAO.findByName(name);
        if (template == null) return notFound(name);
        commandDriverDAO.delete(template.getUuid());
        return ok_empty();
    }

}