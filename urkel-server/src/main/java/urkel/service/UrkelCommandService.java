package urkel.service;

import org.cobbzilla.util.collection.NameAndValue;
import org.cobbzilla.util.handlebars.HandlebarsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.model.UrkelCommand;
import urkel.server.UrkelConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.cobbzilla.util.daemon.DaemonThreadFactory.fixedPool;
import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.daemon.ZillaRuntime.hashOf;
import static org.cobbzilla.util.json.JsonUtil.json;
import static org.cobbzilla.util.system.CommandShell.execScript;

@Service
public class UrkelCommandService {

    @Autowired private UrkelConfiguration configuration;

    private Map<String, Future<String>> futures = new ConcurrentHashMap<>();
    private ExecutorService pool = fixedPool(10);

    public String start(UrkelCommand urkelCommand) {
        final Map ctx = NameAndValue.toMap(urkelCommand.getParams());
        final String script = HandlebarsUtil.apply(configuration.getHandlebars(), urkelCommand.getTemplate().getScript(), ctx);
        final String cacheKey = hashOf(script, urkelCommand.getProfile().getName(), json(NameAndValue.toMap(urkelCommand.getParams())));
        final Map<String, String> env = new HashMap<>(System.getenv());
        if (urkelCommand.hasParams()) {
            for (NameAndValue param : urkelCommand.getParams()) {
                env.put("URK_"+param.getName(), param.getValue());
            }
        }
        futures.computeIfAbsent(cacheKey, k -> pool.submit(() -> execScript(k, env)));
        return cacheKey;
    }

    public String status(String token) {
        // todo: would be nice to show output as it occurs... return a String[] of status lines, accept params on top/bottom/follow...
        final Future<String> future = futures.get(token);
        if (future.isDone()) {
            try {
                return future.get();
            } catch (Exception e) {
                return die("status error: "+e+" (cause="+e.getCause()+")", e);
            }
        }
        return null;
    }
}
