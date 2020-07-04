package org.keiosu.visuturing.core;

public class Configuration {
  private String state;
  private String word;
  private int index;

  public Configuration(String state, String word, int index) {
    this.state = state;
    this.word = word;
    this.index = index;
  }

  Configuration(Configuration other) {
    if (this != other) {
      this.state = other.getState();
      this.word = other.getWord();
      this.index = other.getIndex();
    }
  }

  public String toString() {
    return "(" + state + "," + asTapeRepresentation() + ")";
  }

  private String asTapeRepresentation() {
    if (index < word.length()) {
      return Symbols.LEFT_END_MARKER + word.substring(0, index + 1) + Symbols.CURRENT_CHARACTER_UNDERLINER + word.substring(index + 1);
    } else {
      return Symbols.LEFT_END_MARKER + word.substring(0, index) + Symbols.SPACE + Symbols.CURRENT_CHARACTER_UNDERLINER;
    }
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setWord(String word) {
    this.word = word;
  }

  void setIndex(int index) {
    this.index = index;
  }

  public String getState() {
    return this.state;
  }

  public String getWord() {
    return this.word;
  }

  public int getIndex() {
    return this.index;
  }
}

