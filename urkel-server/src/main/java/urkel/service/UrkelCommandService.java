package urkel.service;

import org.cobbzilla.util.collection.NameAndValue;
import org.cobbzilla.util.handlebars.HandlebarsUtil;
import org.cobbzilla.wizard.model.entityconfig.EntityFieldConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.model.UrkelCommand;
import urkel.model.UrkelCommandStatus;
import urkel.server.UrkelConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.cobbzilla.util.daemon.DaemonThreadFactory.fixedPool;
import static org.cobbzilla.util.daemon.ZillaRuntime.*;
import static org.cobbzilla.util.json.JsonUtil.json;
import static org.cobbzilla.util.security.ShaUtil.sha256_hex;
import static org.cobbzilla.util.system.CommandShell.execScript;
import static org.cobbzilla.wizard.resources.ResourceUtil.invalidEx;

@Service
public class UrkelCommandService {

    @Autowired private UrkelConfiguration configuration;

    private Map<String, Future<String>> futures = new ConcurrentHashMap<>();
    private ExecutorService pool = fixedPool(10);

    public UrkelCommandStatus start(UrkelCommand urkelCommand) {

        // validate params
        final Map ctx = new HashMap();
        final Map<String, String> args = NameAndValue.toMap(urkelCommand.getParams());

        if (urkelCommand.getTemplate().hasInParams()) {
            for (EntityFieldConfig inParam : urkelCommand.getTemplate().getInParams()) {
                final String paramName = inParam.getName();
                final String val = args.get(paramName);
                if (empty(val)) throw invalidEx("err.inParam.empty", "parameter was missing", paramName);
                ctx.put(paramName, val);
            }
        }
        if (ctx.size() != args.size()) {
            throw invalidEx("err.inParam.unknown", "an unknown parameter was provided");
        }

        final String script = HandlebarsUtil.apply(configuration.getHandlebars(), urkelCommand.getTemplate().getScript(), ctx, '<', '>');
        final String cacheKey = sha256_hex(hashOf(script, urkelCommand.getProfile().getName(), json(NameAndValue.toMap(urkelCommand.getParams()))));
        futures.computeIfAbsent(cacheKey, k -> pool.submit(() -> runCommand(script)));
        return new UrkelCommandStatus(cacheKey);
    }

    private String runCommand(String script) {
        // todo
        return execScript(script);
    }

    public UrkelCommandStatus status(String token) {
        // todo: would be nice to show output as it occurs... return a String[] of status lines, accept params on top/bottom/follow...
        final Future<String> future = futures.get(token);
        if (future.isDone()) {
            try {
                return new UrkelCommandStatus(token, future.get());
            } catch (Exception e) {
                return die("status error: "+e+" (cause="+e.getCause()+")", e);
            }
        }
        return new UrkelCommandStatus(token);
    }
}
