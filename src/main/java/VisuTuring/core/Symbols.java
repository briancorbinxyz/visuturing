package VisuTuring.core;

public class Symbols {

  public static final char UNDERLINER = '\u0332';
  public static final char RIGHT_ARROW = '\u2192';
  public static final char LEFT_ARROW = '\u2190';
  public static final char SPACE = '\u2423';
  public static final char LEFT_END_MARKER = '\u201d';
  public static final char ASSERTION = '\u00a6';

  public Symbols() {
  }

  public static String toUnicode(char var0) {
    String var1 = "";
    String var2 = Integer.toHexString(var0 & '\uffff');
    if (var2.length() == 2) {
      var1 = var1 + "00";
    }

    var1 = var1 + var2.toUpperCase();
    return var1;
  }

  public static char unicodeToChar(String var0) {
    char var1 = (char)Integer.parseInt(var0, 16);
    return var1;
  }

  public static String trim(String var0) {
    String var1 = new String(var0);
    if (var0.endsWith(String.valueOf(SPACE))) {
      int var2 = var0.length() - 1;
      char var3 = var0.charAt(var2);

      while(var3 == 8852) {
        var1 = var1.substring(0, var2--);
        if (var2 > -1) {
          var3 = var1.charAt(var2);
        } else {
          var3 = 8883;
        }
      }
    }

    return var1;
  }

  public static void trimToHead(Configuration var0) {
    if (var0.getWord() != null) {
      String var1 = var0.getWord();
      String var2 = new String(var1);
      if (var1.endsWith("⊔")) {
        int var3 = var1.length() - 1;
        char var4 = var1.charAt(var3);

        while(var4 == 8852 && var3 > var0.getIndex()) {
          var2 = var2.substring(0, var3--);
          if (var3 > -1) {
            var4 = var2.charAt(var3);
          } else {
            var4 = 8883;
          }
        }
      }

      var0.setWord(var2);
    }

  }
}

