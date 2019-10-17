package org.keiosu.visuturing;

import java.io.IOException;

public class BrowserControl {
  private static final String WIN_ID = "Windows";

  public BrowserControl() {
  }

  public static void displayURL(String url) {
    String cmd = "";
    try {
      Process cmdProcess;
      if (isWindowsPlatform()) {
        cmd = "rundll32 url.dll,FileProtocolHandler " + url;
        cmdProcess = Runtime.getRuntime().exec(cmd);
      } else {
        cmd = "firefox -new-tab \"" + url + "\"";
        cmdProcess = Runtime.getRuntime().exec(cmd);

        try {
          int exitCode = cmdProcess.waitFor();
          if (exitCode != 0) {
            cmd = "netscape " + url;
            cmdProcess = Runtime.getRuntime().exec(cmd);
          }
        } catch (InterruptedException e) {
          System.err.println("Error bringing up browser, cmd='" + cmd + "'");
          System.err.println("Caught: " + e);
          Thread.currentThread().interrupt();
        }
      }
    } catch (IOException e) {
      System.err.println("Could not invoke browser, command=" + cmd);
      System.err.println("Caught: " + e);
    }
  }

  private static boolean isWindowsPlatform() {
    return System.getProperty("os.name", "").startsWith(WIN_ID);
  }
}
