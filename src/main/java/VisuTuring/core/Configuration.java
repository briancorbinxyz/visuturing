package VisuTuring.core;

public class Configuration {
  private String state;
  private String word;
  private int index;

  public Configuration(String var1, String var2, int var3) {
    this.state = var1;
    this.word = var2;
    this.index = var3;
  }

  public Configuration(Configuration var1) {
    if (this != var1) {
      this.state = var1.getState();
      this.word = var1.getWord();
      this.index = var1.getIndex();
    }

  }

  public String toString() {
    String var1 = "";
    if (this.index < this.word.length()) {
      var1 = '⊳' + this.word.substring(0, this.index + 1) + '̲' + this.word.substring(this.index + 1);
    } else {
      var1 = '⊳' + this.word.substring(0, this.index) + '⊔' + '̲';
    }

    return "(" + this.state + "," + var1 + ")";
  }

  public void setState(String var1) {
    this.state = var1;
  }

  public void setWord(String var1) {
    this.word = var1;
  }

  public void setIndex(int var1) {
    this.index = var1;
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

