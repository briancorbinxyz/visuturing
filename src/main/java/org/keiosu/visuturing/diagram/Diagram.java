package org.keiosu.visuturing.diagram;

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Transition;

public class Diagram {

    public static final double LOOP_LENGTH = 60.0D;
    public static final double STATE_RADIUS = 20.0D;

    public Shape transitionCurve(Transition transition, State state) {
        if (transition.getCurrentState().equals(transition.getNextState())) {
            return internalTransitionCurve(
                    transition.getP1(),
                    transition.getP2(),
                    transition.getControlPoint(),
                    state.getLocation());
        } else {
            return externalTransitionCurve(
                    transition.getP1(), transition.getP2(), transition.getControlPoint());
        }
    }

    public Double transitionPoint(Transition transition, State state, double offset) {
        if (transition.getCurrentState().equals(transition.getNextState())) {
            return internalTransitionPoint(
                    (CubicCurve2D) transitionCurve(transition, state), offset);
        } else {
            return externalTransitionPoint(
                    (QuadCurve2D) transitionCurve(transition, state), offset);
        }
    }

    private CubicCurve2D.Double internalTransitionCurve(
            Point2D start, Point2D end, Point2D cp, Point2D centrePoint) {
        double v =
                Math.sqrt(
                        (cp.getX() - centrePoint.getX()) * (cp.getX() - centrePoint.getX())
                                + (cp.getY() - centrePoint.getY())
                                        * (cp.getY() - centrePoint.getY()));
        double centrePointOffset = Math.sqrt(3600.0D + v * v);
        double atan = Math.atan(LOOP_LENGTH / v);
        double vOffset = v * (cp.getX() - centrePoint.getX());
        double acos = Math.acos(vOffset / (v * v));
        if (centrePoint.getY() < cp.getY()) {
            acos = -acos;
        }

        var controlPoint1 =
                new Point2D.Double(
                        centrePoint.getX() + centrePointOffset * Math.cos(atan - acos),
                        centrePoint.getY() + centrePointOffset * Math.sin(atan - acos));
        var controlPoint2 =
                new Point2D.Double(
                        centrePoint.getX() + centrePointOffset * Math.cos(-atan - acos),
                        centrePoint.getY() + centrePointOffset * Math.sin(-atan - acos));
        setPointToCurveBisection(start, controlPoint1);
        setPointToCurveBisection(end, controlPoint2);
        CubicCurve2D.Double curve = new CubicCurve2D.Double();
        curve.setCurve(start, controlPoint1, controlPoint2, end);
        return curve;
    }

    private QuadCurve2D.Double externalTransitionCurve(
            Point2D start, Point2D end, Point2D controlPoint) {
        setPointToCurveBisection(end, controlPoint);
        setPointToCurveBisection(start, controlPoint);
        QuadCurve2D.Double curve = new QuadCurve2D.Double();
        curve.setCurve(start, controlPoint, end);
        return curve;
    }

    private void setPointToCurveBisection(Point2D point, Point2D controlPoint) {
        double cvx = controlPoint.getX() - point.getX();
        double cvy = controlPoint.getY() - point.getY();
        double curveOffset = STATE_RADIUS / Math.sqrt(cvx * cvx + cvy * cvy);
        point.setLocation(point.getX() + cvx * curveOffset, point.getY() + cvy * curveOffset);
    }

    private Double externalTransitionPoint(QuadCurve2D edge, double offset) {
        double cx =
                (1.0D - offset) * (1.0D - offset) * edge.getX1()
                        + 2.0D * (1.0D - offset) * offset * edge.getCtrlX()
                        + offset * offset * edge.getX2();
        double cy =
                (1.0D - offset) * (1.0D - offset) * edge.getY1()
                        + 2.0D * (1.0D - offset) * offset * edge.getCtrlY()
                        + offset * offset * edge.getY2();
        return new Double(cx - 5.0D, cy - 5.0D, 10.0D, 10.0D);
    }

    private Double internalTransitionPoint(CubicCurve2D edge, double offset) {
        double cx =
                (1.0D - offset) * (1.0D - offset) * (1.0D - offset) * edge.getX1()
                        + 3.0D * (1.0D - offset) * (1.0D - offset) * offset * edge.getCtrlX1()
                        + 3.0D * (1.0D - offset) * offset * offset * edge.getCtrlX2()
                        + offset * offset * offset * edge.getX2();
        double cy =
                (1.0D - offset) * (1.0D - offset) * (1.0D - offset) * edge.getY1()
                        + 3.0D * (1.0D - offset) * (1.0D - offset) * offset * edge.getCtrlY1()
                        + 3.0D * (1.0D - offset) * offset * offset * edge.getCtrlY2()
                        + offset * offset * offset * edge.getY2();
        return new Double(cx - 5.0D, cy - 5.0D, 10.0D, 10.0D);
    }
}
