package org.keiosu.visuturing.core;

public class Symbols {

  public static final char UNDERLINER = '\u0332';
  public static final char RIGHT_ARROW = '\u2192';
  public static final char LEFT_ARROW = '\u2190';
  public static final char SPACE = '\u2423';
  public static final char LEFT_END_MARKER = '\u201d';
  public static final char ASSERTION = '\u00a6';

  private Symbols() {
    // utility
  }

  static String toUnicode(char c) {
    String unicode = "";
    String cAsHex = Integer.toHexString(c & '\uffff');
    if (cAsHex.length() == 2) {
      unicode = unicode + "00";
    }
    return unicode + cAsHex.toUpperCase();
  }

  public static char unicodeToChar(String ch) {
    return (char) Integer.parseInt(ch, 16);
  }

  public static String trim(String word) {
    String var1 = word;
    if (word.endsWith(String.valueOf(SPACE))) {
      int var2 = word.length() - 1;
      char var3 = word.charAt(var2);

      while(var3 == SPACE) {
        var1 = var1.substring(0, var2--);
        if (var2 > -1) {
          var3 = var1.charAt(var2);
        } else {
          var3 = LEFT_END_MARKER;
        }
      }
    }

    return var1;
  }

  public static void trimToHead(Configuration configuration) {
    if (configuration.getWord() != null) {
      String word = configuration.getWord();
      String trimmedWord = word;
      if (word.length() > 0 && word.charAt(word.length() - 1) == SPACE) {
        int index = word.length() - 1;
        char charAtIndex = word.charAt(index);

        while(charAtIndex == SPACE && index > configuration.getIndex()) {
          trimmedWord  = trimmedWord.substring(0, index--);
          if (index > -1) {
            charAtIndex = trimmedWord.charAt(index);
          } else {
            charAtIndex = LEFT_END_MARKER;
          }
        }
      }
      configuration.setWord(trimmedWord);
    }

  }
}

