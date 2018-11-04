package urkel;

import org.cobbzilla.wizard.client.ApiClientBase;
import org.cobbzilla.wizard.client.script.ApiRunner;
import org.cobbzilla.wizard.client.script.ApiRunnerMultiListener;

import static org.cobbzilla.util.daemon.ZillaRuntime.setSystemTimeOffset;

public class UrkelApiRunner extends ApiRunner {

    public UrkelApiRunner(ApiClientBase api, ApiRunnerMultiListener apiListener) {
        super(api, apiListener);
    }

    @Override public void reset() {
        super.reset();
        setSystemTimeOffset(0);
    }

}
