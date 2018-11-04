package urkel.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.cobbzilla.util.collection.StringPrefixTransformer;
import org.cobbzilla.wizard.filters.auth.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.ApiConstants;
import urkel.model.UrkelProfile;
import urkel.server.UrkelConfiguration;

import javax.ws.rs.ext.Provider;
import java.util.*;

import static urkel.ApiConstants.AUTH_ENDPOINT;

@Provider @Service
public class UrkelAuthFilter  extends AuthFilter<UrkelProfile> {

    @Override public String getAuthTokenHeader() { return ApiConstants.API_TOKEN; }
    @Getter private final Set<String> skipAuthPaths = Collections.emptySet();

    @Autowired private UrkelConfiguration configuration;
    @Autowired @Getter private UrkelAuthProvider authProvider;

    private Set<String> prefixSet(String[] paths) {
        final StringPrefixTransformer transformer = new StringPrefixTransformer(configuration.getHttp().getBaseUri());
        final List<String> prefixes = Arrays.asList(paths);
        return new HashSet<>(CollectionUtils.collect(prefixes, transformer));
    }

    @Getter(lazy=true) private final Set<String> skipAuthPrefixes = initSkipAuthPrefixes();
    public Set<String> initSkipAuthPrefixes() {
        return prefixSet(new String[] {
                AUTH_ENDPOINT,
                "/docs"
        });
    }

    @Getter(lazy=true) private final Set<String> adminRequiredPrefixes = initAdminRequiredPrefixes();
    private Set<String> initAdminRequiredPrefixes() { return prefixSet(new String[] {"/admin"}); }

    @Override protected boolean isPermitted(UrkelProfile principal, ContainerRequest request) {
        // if (startsWith(request.getRequestUri().getPath(), getAdminRequiredPrefixes())) return principal.isAdmin();
        return true;
    }
}
