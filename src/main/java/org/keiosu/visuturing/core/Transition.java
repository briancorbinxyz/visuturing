package org.keiosu.visuturing.core;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;
import java.util.Objects;
import org.keiosu.visuturing.xml.XmlElement;

public class Transition implements XmlElement {
    private String currentState;
    private String nextState;
    private char currentSymbol;
    private char task;
    private QuadCurve2D edge;

    public Transition(String currentState, char currentSymbol, String nextState, char task) {
        this(currentState, currentSymbol, nextState, task, new Point(), new Point(), new Point());
    }

    public void setEdge(Shape edge) {
        setEdge((QuadCurve2D) edge);
    }

    public void setEdge(QuadCurve2D edge) {
        this.edge = edge;
    }

    public void setCurrentSymbol(char currentSymbol) {
        this.currentSymbol = currentSymbol;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public void setTask(char task) {
        this.task = task;
    }

    public String getCurrentState() {
        return this.currentState;
    }

    public char getCurrentSymbol() {
        return this.currentSymbol;
    }

    public String getNextState() {
        return this.nextState;
    }

    public char getTask() {
        return this.task;
    }

    public QuadCurve2D getEdge() {
        return this.edge;
    }

    public String toString() {
        return "| "
                + this.currentState
                + " | "
                + this.currentSymbol
                + " || "
                + this.nextState
                + " | "
                + this.task
                + " |";
    }

    public Transition(
            String currentState,
            char currentSymbol,
            String nextState,
            char task,
            Point startPoint,
            Point endPoint,
            Point controlPoint) {
        this.currentState = currentState;
        this.currentSymbol = currentSymbol;
        this.nextState = nextState;
        this.task = task;
        this.edge = new Double();
        this.edge.setCurve(startPoint, controlPoint, endPoint);
    }

    public Transition(Transition other) {
        if (this != other) {
            this.currentState = other.currentState;
            this.currentSymbol = other.currentSymbol;
            this.nextState = other.nextState;
            this.task = other.task;
            this.edge = other.edge;
        }
    }

    public void setP1(Point2D p1) {
        this.edge.setCurve(p1, this.edge.getCtrlPt(), this.edge.getP2());
    }

    public void setP2(Point2D p2) {
        this.edge.setCurve(this.edge.getP1(), this.edge.getCtrlPt(), p2);
    }

    public void setControlPoint(Point2D controlPoint) {
        this.edge.setCurve(this.edge.getP1(), controlPoint, this.edge.getP2());
    }

    public Point2D getP1() {
        return this.edge.getP1();
    }

    public Point2D getP2() {
        return this.edge.getP2();
    }

    public Point2D getControlPoint() {
        return this.edge.getCtrlPt();
    }

    public String toXml() {
        return "<transition>\n"
                + "<current-state>"
                + this.currentState
                + "</current-state>\n"
                + "<current-symbol>"
                + Symbols.toUnicode(this.currentSymbol)
                + "</current-symbol>\n"
                + "<next-state>"
                + this.nextState
                + "</next-state>\n"
                + "<task>"
                + Symbols.toUnicode(this.task)
                + "</task>\n"
                + "<edge>\n"
                + "<p1 "
                + "x='"
                + this.edge.getP1().getX()
                + "' "
                + "y='"
                + this.edge.getP1().getY()
                + "' "
                + "/>\n"
                + "<p2 "
                + "x='"
                + this.edge.getP2().getX()
                + "' "
                + "y='"
                + this.edge.getP2().getY()
                + "' "
                + "/>\n"
                + "<p3 "
                + "x='"
                + this.edge.getCtrlPt().getX()
                + "' "
                + "y='"
                + this.edge.getCtrlPt().getY()
                + "' "
                + "/>\n"
                + "</edge>\n"
                + "</transition>\n";
    }

    boolean isEqualTo(Transition transition) {
        return Objects.equals(transition.currentState, this.currentState)
                && Objects.equals(transition.nextState, this.nextState)
                && transition.currentSymbol == this.currentSymbol
                && transition.task == this.task;
    }
}
