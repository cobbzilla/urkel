package urkel;

import org.cobbzilla.util.collection.SingletonList;
import org.cobbzilla.wizard.client.ApiClientBase;
import org.cobbzilla.wizard.client.script.ApiRunner;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;
import org.cobbzilla.wizard.server.config.factory.StreamConfigurationSource;
import org.cobbzilla.wizardtest.resources.ApiModelTestBase;
import urkel.server.UrkelConfiguration;
import urkel.server.UrkelServer;

import java.util.List;
import java.util.Map;

import static org.cobbzilla.util.system.CommandShell.loadShellExports;
import static urkel.ApiConstants.API_TOKEN;
import static urkel.ApiConstants.ENTITY_CONFIGS_ENDPOINT;
import static urkel.server.UrkelServer.URKEL_SERVER_ENV_FILE;

public abstract class UrkelTestBase extends ApiModelTestBase<UrkelConfiguration, UrkelServer>{

    protected String getTestConfig() { return "urkel-config-test.yml"; }

    @Override protected List<ConfigurationSource> getConfigurations() { return new SingletonList<>(new StreamConfigurationSource(getTestConfig())); }

    @Override protected String getEntityConfigsEndpoint() { return ENTITY_CONFIGS_ENDPOINT; }

    @Override protected ApiRunner getApiRunner() { return new UrkelApiRunner(getApi(), getApiListener()); }

    @Override protected Map<String, String> getServerEnvironment() throws Exception { return loadShellExports(URKEL_SERVER_ENV_FILE); }

    @Override public ApiClientBase getApi() {
        return new ApiClientBase(super.getApi()) {
            @Override public String getTokenHeader() { return API_TOKEN; }
        };
    }

}
