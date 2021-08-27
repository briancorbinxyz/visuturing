package org.keiosu.visuturing.core;

import java.awt.Point;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Point2D;
import org.keiosu.visuturing.xml.XmlElement;

public class State implements XmlElement {
    private String name;
    private Point2D location; // NOSONAR

    public State(String name, Point2D location) {
        this.name = name;
        this.location = location;
    }

    public State(String name) {
        this(name, new Point(50, 50));
    }

    public State(State state) {
        if (this != state) {
            name = state.name;
            location = state.location;
        }
    }

    public boolean contains(Point2D point) {
        Double area = new Double(location.getX() - 20.0D, location.getY() - 20.0D, 40.0D, 40.0D);
        return area.contains(point);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Point2D getLocation() {
        return location;
    }

    public String toString() {
        return name;
    }

    public String toXml() {
        return "<state>\n"
                + "<name>"
                + name
                + "</name>\n"
                + "<location "
                + "x='"
                + location.getX()
                + "' "
                + "y='"
                + location.getY()
                + "' "
                + "/>\n"
                + "</state>\n";
    }
}
