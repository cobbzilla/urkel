package urkel.main;

import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.util.main.BaseMainOptions;
import org.kohsuke.args4j.Option;

import java.io.File;

import static org.cobbzilla.util.daemon.ZillaRuntime.die;
import static org.cobbzilla.util.io.FileUtil.abs;
import static org.cobbzilla.util.io.FileUtil.basename;

public class UrkelPackageOptions extends BaseMainOptions {

    public static final String USAGE_URKEL = "The urkel-file for the plugin to package. Default is to consider the current directory as a plugin, and use its name to look for <name>-urkel.json";
    public static final String OPT_URKEL = "-f";
    public static final String LONGOPT_URKEL= "--f";
    @Option(name=OPT_URKEL, aliases=LONGOPT_URKEL, usage=USAGE_URKEL)
    @Getter @Setter private File urkel;

    public File getUrkelFile() {
        if (urkel != null) return urkel;
        final String pwd = System.getProperty("user.dir");
        final File f = new File(pwd, basename(pwd) + "-urkel.json");
        if (!f.exists() || !f.canRead()) return die("getUrkelFile: "+abs(f)+" does not exist or is unreadable");
        return f;
    }

    protected File driverPart(String part) {
        final File parent = getUrkelFile().getParentFile();
        return new File(parent, basename(part)+"-"+part);
    }

    public File getDdlFile() { return driverPart("ddl.sql"); }
    public File getScriptFile() { return driverPart("invoke.sh"); }
}
