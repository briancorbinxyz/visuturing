package org.keiosu.visuturing.core;

import org.keiosu.visuturing.xml.XmlElement;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Vector;

public class TuringMachine implements XmlElement {
  private String name;
  private String description;
  private Vector transitions;
  private Vector alphabet;
  private Vector states;
  private boolean hasDiagram;
  private boolean changed;

  public TuringMachine(String var1, String var2, Vector var3, Vector var4, Vector var5) {
    this.name = var1;
    this.description = var2;
    this.transitions = var3;
    this.alphabet = var4;
    this.states = var5;
    this.hasDiagram = false;
    this.changed = false;
  }

  public TuringMachine() {
    this.name = "-untitled-";
    this.description = "-enter a description-";
    this.transitions = new Vector();
    this.states = new Vector();
    this.states.add(new State("s"));
    this.states.add(new State("h"));
    this.alphabet = new Vector();
    this.alphabet.add(new String("⊳"));
    this.alphabet.add(new String("⊔"));
    this.hasDiagram = false;
    this.changed = false;
  }

  public boolean isDeterministic() {
    Vector var1 = new Vector(this.alphabet);
    var1.remove("⊳");

    for(int var2 = 0; var2 < this.states.size(); ++var2) {
      State var3 = (State)this.states.get(var2);
      if (!var3.getName().equals("h")) {
        for(int var4 = 0; var4 < var1.size(); ++var4) {
          Configuration var5 = new Configuration(var3.getName(), (String)var1.get(var4), 0);
          Vector var6 = this.getNextConfig(var5);
          if (var6.size() == 0 || var6.size() > 1) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public boolean isChanged() {
    return this.changed;
  }

  public void setChanged(boolean var1) {
    this.changed = var1;
  }

  public boolean hasDiagram() {
    return this.hasDiagram;
  }

  public void setHasDiagram(boolean var1) {
    if (var1 != this.hasDiagram) {
      this.changed = true;
    }

    this.hasDiagram = var1;
  }

  public TuringMachine(TuringMachine var1) {
    if (this != var1) {
      this.name = var1.name;
      this.description = var1.description;
      this.transitions = var1.transitions;
      this.alphabet = var1.alphabet;
      this.states = var1.states;
      this.hasDiagram = var1.hasDiagram;
    }

  }

  public Vector getTransitions(Configuration var1) {
    Vector var2 = new Vector();
    if (var1 == null) {
      return this.transitions;
    } else {
      char var3 = 8852;
      if (var1.getIndex() < 0) {
        var3 = 8883;
      } else if (var1.getWord() == null) {
        var3 = 8852;
        var1.setWord(Character.toString(Symbols.SPACE));
      } else if (var1.getIndex() < var1.getWord().length()) {
        var3 = var1.getWord().charAt(var1.getIndex());
      }

      for(int var4 = 0; var4 < this.transitions.size(); ++var4) {
        Transition var5 = (Transition)this.transitions.get(var4);
        if (var5.getCurrentState().equals(var1.getState()) && var5.getCurrentSymbol() == var3) {
          var2.add(var5);
        }
      }

      return var2;
    }
  }

  public Vector getNextConfig(Configuration var1) {
    Vector var2 = new Vector();
    if (var1 == null) {
      return var2;
    } else {
      char var3 = 8852;
      if (var1.getIndex() < 0) {
        var3 = 8883;
      } else if (var1.getWord() == null) {
        var3 = 8852;
        var1.setWord(Character.toString(Symbols.SPACE));
      } else if (var1.getIndex() < var1.getWord().length()) {
        var3 = var1.getWord().charAt(var1.getIndex());
      }

      for(int var4 = 0; var4 < this.transitions.size(); ++var4) {
        Transition var5 = (Transition)this.transitions.get(var4);
        if (var5.getCurrentState().equals(var1.getState()) && var5.getCurrentSymbol() == var3) {
          Configuration var6 = new Configuration(var1);
          var6.setState(var5.getNextState());
          String var7;
          if (var5.getTask() != 8594 && var3 != 8883) {
            if (var5.getTask() == 8592) {
              var6.setIndex(var6.getIndex() - 1);
            } else {
              var7 = "";
              var7 = var6.getWord().substring(0, var6.getIndex()) + var5.getTask();
              if (var6.getIndex() < var6.getWord().length() - 1) {
                var7 = var7 + var6.getWord().substring(var6.getIndex() + 1);
              }

              var6.setWord(var7);
            }
          } else {
            var6.setIndex(var6.getIndex() + 1);
            if (var6.getIndex() > var6.getWord().length() - 1) {
              var7 = var6.getWord() + Symbols.SPACE;
              var6.setWord(var7);
            }
          }

          if (var6.getIndex() > var6.getWord().length() - 1) {
            var6.setWord(var6.getWord() + Symbols.SPACE);
          }

          if (var6 != null) {
            Symbols.trimToHead(var6);
          }

          var2.add(var6);
        }
      }

      if (var3 == 8883 && var2.size() == 0) {
        Configuration var8 = new Configuration(var1);
        var8.setIndex(0);
        var2.add(var8);
      }

      return var2;
    }
  }

  public void setName(String var1) {
    if (!var1.equals(this.name)) {
      this.changed = true;
    }

    this.name = var1;
  }

  public void setDescription(String var1) {
    if (!var1.equals(this.description)) {
      this.changed = true;
    }

    this.description = var1;
  }

  public void setTransitions(Vector var1) {
    this.changed = true;
    this.transitions = var1;
  }

  public void setAlphabet(Vector var1) {
    this.changed = true;
    this.alphabet = var1;
  }

  public void setStates(Vector var1) {
    this.changed = true;
    this.states = var1;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public Vector getTransitions() {
    return this.transitions;
  }

  public Vector getAlphabet() {
    return this.alphabet;
  }

  public Vector getStates() {
    return this.states;
  }

  public void addTransition(Transition var1) {
    if (var1 != null && this.getEqualTransition(var1) == null) {
      this.changed = true;
      this.transitions.add(var1);
    }

  }

  public void removeTransition(Transition var1) {
    this.changed = true;
    this.transitions.remove(var1);
  }

  public State getState(String var1) {
    for(int var2 = 0; var2 < this.states.size(); ++var2) {
      State var3 = (State)this.states.get(var2);
      if (var3.getName().equals(var1)) {
        return var3;
      }
    }

    return null;
  }

  public String toXml() {
    StringBuffer var1 = new StringBuffer();
    var1.append("<turing-machine>\n");
    var1.append("<name>" + this.name + "</name>\n");
    var1.append("<description>" + this.description + "</description>\n");
    var1.append("<has-diagram>" + this.hasDiagram + "</has-diagram>\n");
    var1.append("<states>\n");

    int var2;
    for(var2 = 0; var2 < this.states.size(); ++var2) {
      State var3 = (State)this.states.get(var2);
      var1.append(var3.toXml());
    }

    var1.append("</states>\n");
    var1.append("<alphabet>\n");

    for(var2 = 0; var2 < this.alphabet.size(); ++var2) {
      String var4 = (String)this.alphabet.get(var2);
      var1.append("<symbol>" + Symbols.toUnicode(var4.charAt(0)) + "</symbol>\n");
    }

    var1.append("</alphabet>\n");
    var1.append("<transitions>\n");

    for(var2 = 0; var2 < this.transitions.size(); ++var2) {
      Transition var5 = (Transition)this.transitions.get(var2);
      var1.append(var5.toXml());
    }

    var1.append("</transitions>\n");
    var1.append("</turing-machine>\n");
    return var1.toString();
  }

  public void generateDiagram() {
    int var5 = this.states.size();
    int var6 = 50 * var5;
    HashMap var7 = new HashMap();

    int var8;
    for(var8 = 0; var8 < var5; ++var8) {
      State var9 = (State)this.states.get(var8);
      var9.setLocation(new Point(50 + var8 * 5 * 40, var6));
    }

    var8 = this.transitions.size();

    for(int var20 = 0; var20 < var8; ++var20) {
      Transition var10 = (Transition)this.transitions.get(var20);
      double var11 = this.getState(var10.getCurrentState()).getLocation().getX();
      double var13 = this.getState(var10.getNextState()).getLocation().getX();
      Double var15 = (Double)var7.get(new Double(var11 + var13));
      if (var15 == null) {
        var15 = new Double(1.0D);
      } else {
        var15 = new Double(var15 + 1.0D);
      }

      var7.put(new Double(var11 + var13), var15);
      Point2D var16 = this.getState(var10.getCurrentState()).getLocation();
      Point2D var18 = this.getState(var10.getNextState()).getLocation();
      Object var17;
      if (!var10.getCurrentState().equals(var10.getNextState())) {
        var17 = new Point((int)(var11 + (var13 - var11) / 2.0D), (int)((double)var6 + (var13 - var11) / var15 / 2.0D));
      } else if (var15 % 2.0D == 0.0D) {
        var17 = new java.awt.geom.Point2D.Double(var11, (double)var6 - var15 * 60.0D);
      } else {
        var17 = new java.awt.geom.Point2D.Double(var11, (double)var6 + var15 * 60.0D);
      }

      java.awt.geom.QuadCurve2D.Double var19 = new java.awt.geom.QuadCurve2D.Double();
      var19.setCurve(var16, (Point2D)var17, var18);
      var10.setEdge(var19);
    }

    this.hasDiagram = true;
    this.changed = true;
  }

  public Transition getEqualTransition(Transition var1) {
    for(int var2 = 0; var2 < this.transitions.size(); ++var2) {
      Transition var3 = (Transition)this.transitions.get(var2);
      if (var3.isEqualTo(var1)) {
        return var3;
      }
    }

    return null;
  }
}
