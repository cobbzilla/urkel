package urkel.server;

import org.cobbzilla.wizard.server.RestServerBase;
import org.cobbzilla.wizard.server.RestServerLifecycleListener;
import org.cobbzilla.wizard.server.config.factory.ConfigurationSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.cobbzilla.util.system.CommandShell.loadShellExports;

public class UrkelServer extends RestServerBase<UrkelConfiguration> {

    private static final List<RestServerLifecycleListener> LISTENERS = new ArrayList<>();

    public static final String URKEL_SERVER_ENV = System.getProperty("user.home") + File.separator + ".urkel.env";
    public static final File URKEL_SERVER_ENV_FILE = new File(URKEL_SERVER_ENV);

    public static final String[] API_CONFIG_YML = {"urkel-config.yml"};
    protected static List<ConfigurationSource> getConfigurationSources() { return getStreamConfigurationSources(UrkelServer.class, API_CONFIG_YML); }

    public static void main (String[] args) throws Exception {
        final List<ConfigurationSource> configSources = getConfigurationSources();
        final Map<String, String> env = System.getenv();
        if (env.get("URKEL_ENV") != null) env.putAll(loadShellExports(URKEL_SERVER_ENV_FILE));
        main(args, UrkelServer.class, LISTENERS, configSources, env);
    }
}
