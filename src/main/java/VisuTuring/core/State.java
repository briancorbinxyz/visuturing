package VisuTuring.core;

import VisuTuring.xml.XmlElement;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D.Double;

public class State implements XmlElement {
  private String name;
  private Point2D location;

  public State(String var1, Point2D var2) {
    this.name = var1;
    this.location = var2;
  }

  public State(String var1) {
    this(var1, new Point(50, 50));
  }

  public State(State var1) {
    if (this != var1) {
      this.name = var1.name;
      this.location = var1.location;
    }

  }

  public boolean contains(Point2D var1) {
    Double var2 = new Double(this.location.getX() - 20.0D, this.location.getY() - 20.0D, 40.0D, 40.0D);
    return var2.contains(var1);
  }

  public void setName(String var1) {
    this.name = var1;
  }

  public void setLocation(Point2D var1) {
    this.location = var1;
  }

  public String getName() {
    return this.name;
  }

  public Point2D getLocation() {
    return this.location;
  }

  public String toString() {
    return this.name;
  }

  public String toXml() {
    String var1 = "<state>\n";
    var1 = var1 + "<name>" + this.name + "</name>\n" + "<location " + "x='" + this.location.getX() + "' " + "y='" + this.location.getY() + "' " + "/>\n" + "</state>\n";
    return var1;
  }
}
