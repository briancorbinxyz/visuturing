package org.keiosu.visuturing;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
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
        } catch (Exception e) {
            LOGGER.atError()
                    .setCause(e)
                    .log("Could not invoke configured browser, error={}", e.getMessage());
        }
    }

    private static void openUrlFirefox(String url) throws IOException {
        URL fullUrl = new URL(url);
        Process cmdProcess =
                Runtime.getRuntime().exec(new String[] {"firefox", "-new-tab", fullUrl.toString()});
        try {
            cmdProcess.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.atError()
                    .setCause(e)
                    .log("Could not invoke firefox browser, error={}", e.getMessage());
        }
    }

    private static void openUrlWindows(String url) throws IOException {
        URL fullUrl = new URL(url);
        Runtime.getRuntime()
                .exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", fullUrl.toString()});
    }

    private static boolean isWindowsPlatform() {
        return System.getProperty("os.name", "").startsWith(WIN_ID);
    }
}
