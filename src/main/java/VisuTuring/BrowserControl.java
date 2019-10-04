package VisuTuring;

import java.io.IOException;

public class BrowserControl {
  private static final String WIN_ID = "Windows";
  private static final String WIN_PATH = "rundll32";
  private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
  private static final String UNIX_PATH = "netscape";
  private static final String UNIX_FLAG = "-remote openURL";

  public BrowserControl() {
  }

  public static void displayURL(String var0) {
    boolean var1 = isWindowsPlatform();
    String var2 = null;

    try {
      Process var3;
      if (var1) {
        var2 = "rundll32 url.dll,FileProtocolHandler " + var0;
        var3 = Runtime.getRuntime().exec(var2);
      } else {
        var2 = "netscape -remote openURL(" + var0 + ")";
        var3 = Runtime.getRuntime().exec(var2);

        try {
          int var4 = var3.waitFor();
          if (var4 != 0) {
            var2 = "netscape " + var0;
            var3 = Runtime.getRuntime().exec(var2);
          }
        } catch (InterruptedException var5) {
          System.err.println("Error bringing up browser, cmd='" + var2 + "'");
          System.err.println("Caught: " + var5);
        }
      }
    } catch (IOException var6) {
      System.err.println("Could not invoke browser, command=" + var2);
      System.err.println("Caught: " + var6);
    }

  }

  public static boolean isWindowsPlatform() {
    String var0 = System.getProperty("os.name");
    return var0 != null && var0.startsWith("Windows");
  }
}
