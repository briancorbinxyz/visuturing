package org.keiosu.visuturing.core;

public class Symbols {

  public static final String STATE_BEGINNING_STATE = "s";
  public static final String STATE_HALTING_STATE = "h";

  static final char CURRENT_CHARACTER_UNDERLINER = '\u0332';

  public static final char RIGHT_ARROW = '\u2192';
  public static final char LEFT_ARROW = '\u2190';
  public static final char SPACE = '\u2423';
  public static final char LEFT_END_MARKER = '\u22B3';
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
    return word == null || word.isEmpty() ? word : trim(word, 0);
  }

  public static void trimToHead(Configuration configuration) {
    if (configuration.getWord() != null) {
      String trimmedWord = trim(configuration.getWord(), configuration.getIndex());
      configuration.setWord(trimmedWord);
    }
  }

  private static String trim(String word, int minIndex) {
    String trimmedWord = word;
    if (!word.isEmpty() && word.charAt(word.length() - 1) == SPACE) {
      int index = word.length() - 1;
      char charAtIndex = word.charAt(index);

      while(charAtIndex == SPACE && index > minIndex) {
        trimmedWord  = trimmedWord.substring(0, index--);
        if (index > -1) {
          charAtIndex = trimmedWord.charAt(index);
        } else {
          charAtIndex = LEFT_END_MARKER;
        }
      }
    }
    return trimmedWord;
  }
}

