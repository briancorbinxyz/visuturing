package org.keiosu.visuturing;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserControl {

    private BrowserControl() {
        // private
    }

    private static final String WIN_ID = "Windows";

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void displayURL(String url) {
        try {
            if (isWindowsPlatform()) {
                openUrlWindows(url);
            } else {
                openUrlFirefox(url);
            }
        } catch (IOException e) {
            LOGGER.atError()
                    .addArgument(e.getMessage())
                    .addArgument(e)
                    .log("Could not invoke browser, error={}");
        }
    }

    private static void openUrlFirefox(String url) throws IOException {
        String cmd = "firefox -new-tab \"" + url + "\"";
        Process cmdProcess = Runtime.getRuntime().exec(cmd);
        try {
            cmdProcess.waitFor();
        } catch (InterruptedException e) {
            LOGGER.atError()
                    .addArgument(e.getMessage())
                    .addArgument(e)
                    .log("Could not invoke browser, error={}");
            Thread.currentThread().interrupt();
        }
    }

    private static void openUrlWindows(String url) throws IOException {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }

    private static boolean isWindowsPlatform() {
        return System.getProperty("os.name", "").startsWith(WIN_ID);
    }
}
