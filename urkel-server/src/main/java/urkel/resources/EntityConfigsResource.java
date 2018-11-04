package urkel.resources;


import com.sun.jersey.api.core.HttpContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cobbzilla.wizard.resources.AbstractEntityConfigsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.server.UrkelConfiguration;

import javax.ws.rs.Path;

import static urkel.ApiConstants.ENTITY_CONFIGS_ENDPOINT;

@Path(ENTITY_CONFIGS_ENDPOINT)
@Service @Slf4j
public class EntityConfigsResource extends AbstractEntityConfigsResource {

    @Getter(AccessLevel.PROTECTED) @Autowired private UrkelConfiguration configuration;

    @Override protected boolean authorized(HttpContext ctx) { return true; }

}
