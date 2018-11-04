package urkel;

import org.junit.Test;

public class UrkelCommandTest extends UrkelTestBase {

    private static final String DOC_CTX = "Command Test";

    @Override protected String getModelPrefix() { return "models/simple/"; }

    @Test public void testSimpleCommand () throws Exception {
        apiDocs.startRecording(DOC_CTX, "Register a simple command and changes all numbers to underscores and shows the result");
        modelTest("echo");
    }
}
