package urkel.main;

import org.cobbzilla.util.io.FileUtil;
import org.cobbzilla.util.main.BaseMain;
import urkel.model.UrkelCommandDriver;

import java.io.File;

import static org.cobbzilla.util.json.JsonUtil.json;

public class UrkelPackageMain extends BaseMain<UrkelPackageOptions> {

    public static void main (String[] args) { main(UrkelPackageMain.class, args); }

    @Override protected void run() throws Exception {

        final UrkelPackageOptions options = getOptions();
        final UrkelCommandDriver driver = json(FileUtil.toString(options.getUrkelFile()), UrkelCommandDriver.class);

        final File script = options.getScriptFile();
        if (script.exists()) driver.setScript(FileUtil.toString(script));

    }
}
