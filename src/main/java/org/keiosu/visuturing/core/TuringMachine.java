package org.keiosu.visuturing.core;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import org.keiosu.visuturing.xml.XmlElement;

/** The star of the show! */
public class TuringMachine implements XmlElement {

  private static final String DEFAULT_MACHINE_NAME = "-untitled-";
  private static final String DEFAULT_MACHINE_DESCRIPTION = "-enter a description-";

  private String name;
  private String description;
  private List<Transition> transitions;
  private List<String> alphabet;
  private List<State> states;
  private boolean hasDiagram;
  private transient volatile boolean changed; // NOSONAR

  public TuringMachine(
      String name,
      String description,
      List<Transition> transitions,
      List<String> alphabet,
      List<State> states) {
    this.name = name;
    this.description = description;
    this.transitions = transitions;
    this.alphabet = alphabet;
    this.states = states;
  }

  public TuringMachine() {
    this.name = DEFAULT_MACHINE_NAME;
    this.description = DEFAULT_MACHINE_DESCRIPTION;
    this.transitions = new ArrayList<>();
    this.states = new ArrayList<>();
    this.states.add(new State(Symbols.STATE_BEGINNING_STATE));
    this.states.add(new State(Symbols.STATE_HALTING_STATE));
    this.alphabet = List.of(String.valueOf(Symbols.LEFT_END_MARKER), String.valueOf(Symbols.SPACE));
  }

  public boolean isDeterministic() {
    List<String> tempAlphabet = new ArrayList<>(this.alphabet);
    tempAlphabet.remove(String.valueOf(Symbols.LEFT_END_MARKER));

    for (State state : this.states) {
      if (!state.getName().equals(Symbols.STATE_HALTING_STATE)) {
        for (String s : tempAlphabet) {
          Configuration configuration = new Configuration(state.getName(), s, 0);
          List<Configuration> possibleNextConfigurations = this.getNextConfig(configuration);
          if (possibleNextConfigurations.size() != 1) {
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

  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  public boolean hasDiagram() {
    return this.hasDiagram;
  }

  public void setHasDiagram(boolean hasDiagram) {
    if (hasDiagram != this.hasDiagram) {
      this.changed = true;
    }

    this.hasDiagram = hasDiagram;
  }

  public TuringMachine(TuringMachine other) {
    if (this != other) {
      this.name = other.name;
      this.description = other.description;
      this.transitions = new ArrayList<>(other.transitions);
      this.alphabet = new ArrayList<>(other.alphabet);
      this.states = new ArrayList<>(other.states);
      this.hasDiagram = other.hasDiagram;
    }
  }

  public List<Transition> nextTransitionsFor(Configuration configuration) {
    List<Transition> possibleTransitions = new ArrayList<>();
    if (configuration == null) {
      return this.transitions;
    } else {
      for (Transition transition : this.transitions) {
        if (transition.getCurrentState().equals(configuration.getState())
            && transition.getCurrentSymbol() == currentSymbolFor(configuration)) {
          possibleTransitions.add(transition);
        }
      }

      return possibleTransitions;
    }
  }

  public List<Configuration> getNextConfig(Configuration configuration) {
    if (configuration == null) {
      return Collections.emptyList();
    }

    List<Configuration> possibleConfigurations = new ArrayList<>();
    char nextSymbol = currentSymbolFor(configuration);
    for (Transition transition : transitions) {
      if (transition.getCurrentState().equals(configuration.getState())
          && transition.getCurrentSymbol() == nextSymbol) {
        Configuration nextConfiguration =
            computeNextConfigurationFrom(configuration, nextSymbol, transition);

        if (nextConfiguration.getIndex() > nextConfiguration.getWord().length() - 1) {
          nextConfiguration.setWord(nextConfiguration.getWord() + Symbols.SPACE);
        }

        Symbols.trimToHead(nextConfiguration);
        possibleConfigurations.add(nextConfiguration);
      }
    }

    if (nextSymbol == Symbols.LEFT_END_MARKER && possibleConfigurations.isEmpty()) {
      Configuration defaultConfiguration = new Configuration(configuration);
      defaultConfiguration.setIndex(0);
      possibleConfigurations.add(defaultConfiguration);
    }

    return possibleConfigurations;
  }

  private Configuration computeNextConfigurationFrom(
      Configuration configuration, char nextSymbol, Transition transition) {
    Configuration nextConfiguration = new Configuration(configuration);
    nextConfiguration.setState(transition.getNextState());
    String word;
    if (transition.getTask() != Symbols.RIGHT_ARROW && nextSymbol != Symbols.LEFT_END_MARKER) {
      if (transition.getTask() == Symbols.LEFT_ARROW) {
        nextConfiguration.setIndex(nextConfiguration.getIndex() - 1);
      } else {
        word =
            nextConfiguration.getWord().substring(0, nextConfiguration.getIndex())
                + transition.getTask();
        if (nextConfiguration.getIndex() < nextConfiguration.getWord().length() - 1) {
          word = word + nextConfiguration.getWord().substring(nextConfiguration.getIndex() + 1);
        }
        nextConfiguration.setWord(word);
      }
    } else {
      nextConfiguration.setIndex(nextConfiguration.getIndex() + 1);
      if (nextConfiguration.getIndex() > nextConfiguration.getWord().length() - 1) {
        word = nextConfiguration.getWord() + Symbols.SPACE;
        nextConfiguration.setWord(word);
      }
    }
    return nextConfiguration;
  }

  private char currentSymbolFor(Configuration configuration) {
    if (configuration.getIndex() < 0) {
      return Symbols.LEFT_END_MARKER;
    } else if (configuration.getWord() == null) {
      configuration.setWord(String.valueOf(Symbols.SPACE));
      return Symbols.SPACE;
    } else if (configuration.getIndex() < configuration.getWord().length()) {
      return configuration.getWord().charAt(configuration.getIndex());
    } else {
      return Symbols.SPACE;
    }
  }

  public void setName(String name) {
    if (!name.equals(this.name)) {
      this.changed = true;
    }
    this.name = name;
  }

  public void setDescription(String description) {
    if (!description.equals(this.description)) {
      this.changed = true;
    }
    this.description = description;
  }

  public void setTransitions(List<Transition> transitions) {
    this.changed = true;
    this.transitions = transitions;
  }

  public void setAlphabet(List<String> alphabet) {
    this.changed = true;
    this.alphabet = alphabet;
  }

  public void setStates(List<State> states) {
    this.changed = true;
    this.states = states;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public List<Transition> getTransitions() {
    return this.transitions;
  }

  public List<String> getAlphabet() {
    return this.alphabet;
  }

  public List<State> getStates() {
    return this.states;
  }

  public void addTransition(Transition transition) {
    if (transition != null && this.getEqualTransition(transition) == null) {
      this.changed = true;
      this.transitions.add(transition);
    }
  }

  public void removeTransition(Transition transition) {
    this.changed = true;
    this.transitions.remove(transition);
  }

  public State stateFor(String stateAsString) {
    for (State state : this.states) {
      if (state.getName().equals(stateAsString)) {
        return state;
      }
    }

    return null;
  }

  public String toXml() {
    StringBuilder xml = new StringBuilder();
    xml.append("<turing-machine>\n");
    xml.append("<name>").append(this.name).append("</name>\n");
    xml.append("<description>").append(this.description).append("</description>\n");
    xml.append("<has-diagram>").append(this.hasDiagram).append("</has-diagram>\n");
    xml.append("<states>\n");
    for (State state : this.states) {
      xml.append(state.toXml());
    }
    xml.append("</states>\n");
    xml.append("<alphabet>\n");
    for (String symbol : this.alphabet) {
      xml.append("<symbol>").append(Symbols.toUnicode(symbol.charAt(0))).append("</symbol>\n");
    }
    xml.append("</alphabet>\n");
    xml.append("<transitions>\n");
    for (Transition transition : this.transitions) {
      xml.append(transition.toXml());
    }
    xml.append("</transitions>\n");
    xml.append("</turing-machine>\n");
    return xml.toString();
  }

  public void generateDiagram() {
    int noStates = this.states.size();
    int maxHeight = 50 * noStates;
    Map<Double, Double> spacingMap = new HashMap<>();

    for (int i = 0; i < noStates; ++i) {
      this.states.get(i).setLocation(new Point(50 + i * 5 * 40, maxHeight));
    }

    for (Transition transition : this.transitions) {
      double xFromState = this.stateFor(transition.getCurrentState()).getLocation().getX();
      double xToState = this.stateFor(transition.getNextState()).getLocation().getX();
      Double curveRatio = spacingMap.get(xFromState + xToState);
      if (curveRatio == null) {
        curveRatio = 1.0D;
      } else {
        curveRatio = curveRatio + 1.0D;
      }
      spacingMap.put(xFromState + xToState, curveRatio);
      Point2D locationFromState = this.stateFor(transition.getCurrentState()).getLocation();
      Point2D locationToState = this.stateFor(transition.getNextState()).getLocation();
      Point2D curveCenterPoint;
      if (!transition.getCurrentState().equals(transition.getNextState())) {
        curveCenterPoint =
            new Point(
                (int) (xFromState + (xToState - xFromState) / 2.0D),
                (int) ((double) maxHeight + (xToState - xFromState) / curveRatio / 2.0D));
      } else if (curveRatio % 2.0D == 0.0D) {
        curveCenterPoint = new Point2D.Double(xFromState, (double) maxHeight - curveRatio * 60.0D);
      } else {
        curveCenterPoint = new Point2D.Double(xFromState, (double) maxHeight + curveRatio * 60.0D);
      }

      QuadCurve2D.Double transitionEdge = new QuadCurve2D.Double();
      transitionEdge.setCurve(locationFromState, curveCenterPoint, locationToState);
      transition.setEdge(transitionEdge);
    }
    this.hasDiagram = true;
    this.changed = true;
  }

  public Transition getEqualTransition(Transition other) {
    for (Transition transition : this.transitions) {
      if (transition.isEqualTo(other)) {
        return transition;
      }
    }

    return null;
  }
}
