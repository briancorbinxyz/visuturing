package org.keiosu.visuturing.core;

import org.keiosu.visuturing.xml.XmlElement;

import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.QuadCurve2D.Double;

public class Transition implements XmlElement {
  private String currentState;
  private char currentSymbol;
  private String nextState;
  private char task;
  private QuadCurve2D edge;

  public Transition(String var1, char var2, String var3, char var4) {
    this(var1, var2, var3, var4, new Point(), new Point(), new Point());
  }

  public void setEdge(QuadCurve2D var1) {
    this.edge = var1;
  }

  public void setCurrentState(String var1) {
    this.currentState = var1;
  }

  public void setCurrentSymbol(char var1) {
    this.currentSymbol = var1;
  }

  public void setNextState(String var1) {
    this.nextState = var1;
  }

  public void setTask(char var1) {
    this.task = var1;
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
    return "| " + this.currentState + " | " + this.currentSymbol + " || " + this.nextState + " | " + this.task + " |";
  }

  public Transition(String var1, char var2, String var3, char var4, Point var5, Point var6, Point var7) {
    this.currentState = var1;
    this.currentSymbol = var2;
    this.nextState = var3;
    this.task = var4;
    this.edge = new Double();
    this.edge.setCurve(var5, var7, var6);
  }

  public Transition(Transition var1) {
    if (this != var1) {
      this.currentState = var1.currentState;
      this.currentSymbol = var1.currentSymbol;
      this.nextState = var1.nextState;
      this.task = var1.task;
      this.edge = var1.edge;
    }

  }

  public void setP1(Point2D var1) {
    this.edge.setCurve(var1, this.edge.getCtrlPt(), this.edge.getP2());
  }

  public void setP2(Point2D var1) {
    this.edge.setCurve(this.edge.getP1(), this.edge.getCtrlPt(), var1);
  }

  public void setControlPoint(Point2D var1) {
    this.edge.setCurve(this.edge.getP1(), var1, this.edge.getP2());
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

  public boolean contains(Point2D var1) {
    BasicStroke var2 = new BasicStroke(5.0F);
    return var2.createStrokedShape(this.edge).contains(var1);
  }

  public String toXml() {
    String var1 = "<transition>\n";
    var1 = var1 + "<current-state>" + this.currentState + "</current-state>\n" + "<current-symbol>" + Symbols.toUnicode(this.currentSymbol) + "</current-symbol>\n" + "<next-state>" + this.nextState + "</next-state>\n" + "<task>" + Symbols.toUnicode(this.task) + "</task>\n" + "<edge>\n" + "<p1 " + "x='" + this.edge.getP1().getX() + "' " + "y='" + this.edge.getP1().getY() + "' " + "/>\n" + "<p2 " + "x='" + this.edge.getP2().getX() + "' " + "y='" + this.edge.getP2().getY() + "' " + "/>\n" + "<p3 " + "x='" + this.edge.getCtrlPt().getX() + "' " + "y='" + this.edge.getCtrlPt().getY() + "' " + "/>\n" + "</edge>\n" + "</transition>\n";
    return var1;
  }

  public boolean isEqualTo(Transition var1) {
    return var1.currentState == this.currentState && var1.nextState == this.nextState && var1.currentSymbol == this.currentSymbol && var1.task == this.task;
  }
}
