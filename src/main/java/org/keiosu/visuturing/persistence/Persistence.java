package org.keiosu.visuturing.persistence;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.xml.XmlElement;
import org.keiosu.visuturing.xml.XmlTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Persistence {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void exportToHTML(XmlElement xmlElement, String filename)
        throws IOException, TransformerException {
        XmlTransformer xmlTransformer = new XmlTransformer("web/tm.xsl");
        String htmlFilename = filename;
        if (!htmlFilename.endsWith("html") && !htmlFilename.endsWith("htm")) {
            htmlFilename = htmlFilename + ".html";
        }

        File htmlFile = new File(htmlFilename);
        if (htmlFile.createNewFile()) {
           LOGGER.atInfo().log("Created new file: {}", htmlFilename);
        }
        try (FileOutputStream file = new FileOutputStream(htmlFile)) {
            xmlTransformer.transform(xmlElement, file);
        }
    }

    private Persistence() {
        // do nothing
    }

    private static String trimExtension(String filename) {
        int indexOfExtension = filename.indexOf(".");
        return indexOfExtension < 0 ? filename : filename.substring(0, indexOfExtension);
    }

    public static void save(XmlElement xmlElement, String filename) throws IOException {
        save(xmlElement, new File(filename));
    }

    public static void save(XmlElement xmlElement, File file) throws IOException {
        String filename = trimExtension(file.toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename.concat(".vt")))) {
          String header =
              "<?xml version=\"1.0\" ?>\n\n<?author name='Brian L A Corbin' ?>\n<?program name='VisuTuring' version='1.0 3YP' ?>";
          writer.write(header + xmlElement.toXml());
        }
    }

    public static TuringMachine load(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Element element = builder.parse(file).getDocumentElement();
        return retrieveTuringMachine(element);
    }

    private static TuringMachine retrieveTuringMachine(Element machineElement) {
        String machineName = "<no-name>";
        String machineDescription = "none";
        boolean machineHasDiagram = false;
        List<State> machineStates = new ArrayList<>();
        List<String> machineAlphabet = new ArrayList<>();
        List<Transition> machineTransitions = new ArrayList<>();
        NodeList nodes = machineElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                Text text = (Text) element.getFirstChild();
                switch (element.getTagName()) {
                    case "name":
                        machineName = text.getData();
                        break;
                    case "description":
                        machineDescription = text.getData();
                        break;
                    case "has-diagram":
                        machineHasDiagram = text.getData().equals("true");
                        break;
                    case "states":
                        machineStates = getStates(element);
                        break;
                    case "alphabet":
                        machineAlphabet = getAlphabet(element);
                        break;
                    case "transitions":
                        machineTransitions = getTransitions(element);
                        break;
                }
            }
        }

        TuringMachine machine = new TuringMachine(machineName, machineDescription, machineTransitions,
            machineAlphabet,
            machineStates);
        machine.setHasDiagram(machineHasDiagram);
        return machine;
    }

    private static List<State> getStates(Element element) {
        List<State> states = new ArrayList<>();
        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element n = (Element) node;
                if (n.getTagName().equals("state")) {
                    states.add(getState(n, (i + 1) * 50));
                }
            }
        }

        return states;
    }

    private static State getState(Element stateElement, double defaultOffset) {
        NodeList nodes = stateElement.getChildNodes();

        String text = "";
        double xOffset = defaultOffset;
        double yOffset = 50.0D;
        for (int j = 0; j < nodes.getLength(); ++j) {
            Node node = nodes.item(j);
            if (node instanceof Element) {
                Element n = (Element) node;
                if (n.getTagName().equals("name")) {
                    text = ((Text) n.getFirstChild()).getData();
                } else if (n.getTagName().equals("location")) {
                    xOffset = Double.parseDouble(n.getAttribute("x"));
                    yOffset = Double.parseDouble(n.getAttribute("y"));
                }
            }
        }

        return new State(text, new Point((int) xOffset, (int) yOffset));
    }

    private static List<String> getAlphabet(Element alphabetElement) {
        List<String> alphabet = new ArrayList<>();
        NodeList nodes = alphabetElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                Text text = (Text) element.getFirstChild();
                if (element.getTagName().equals("symbol")) {
                    alphabet.add("" + Symbols.unicodeToChar(text.getData()));
                }
            }
        }

        return alphabet;
    }

    private static List<Transition> getTransitions(Element transitionElement) {
        List<Transition> transitions = new ArrayList<>();
        NodeList nodes = transitionElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().equals("transition")) {
                    transitions.add(getTransition(element));
                }
            }
        }

        return transitions;
    }

    private static Transition getTransition(Element element) {
        NodeList transitionNodes = element.getChildNodes();

        String fromState = "";
        String toState = "";
        char currentSymbol = ' ';
        char task = ' ';
        Shape curve = new QuadCurve2D.Double();
        for (int i = 0; i < transitionNodes.getLength(); ++i) {
            Node node = transitionNodes.item(i);
            if (node instanceof Element) {
                Element n = (Element) node;
                Text text = (Text) n.getFirstChild();
                if (n.getTagName().equals("current-state")) {
                    fromState = text.getData();
                } else {
                    switch (n.getTagName()) {
                        case "current-symbol":
                            currentSymbol = Symbols.unicodeToChar(text.getData());
                            break;
                        case "next-state":
                            toState = text.getData();
                            break;
                        case "task":
                            task = Symbols.unicodeToChar(text.getData());
                            break;
                        case "edge":
                            curve = getEdge(n);
                            break;
                    }
                }
            }
        }

        Transition transition = new Transition(fromState, currentSymbol, toState, task);
        transition.setEdge(curve);
        return transition;
    }

    private static QuadCurve2D getEdge(Element curveElement) {
        java.awt.geom.Point2D.Double startPoint = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D.Double endPoint = new java.awt.geom.Point2D.Double();
        java.awt.geom.Point2D.Double cp1 = new java.awt.geom.Point2D.Double();
        NodeList nodes = curveElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element n = (Element) node;
                n.getFirstChild();
                switch (n.getTagName()) {
                    case "p1":
                        startPoint =
                            new java.awt.geom.Point2D.Double(
                                Double.parseDouble(n.getAttribute("x")),
                                Double.parseDouble(n.getAttribute("y")));
                        break;
                    case "p2":
                        endPoint =
                            new java.awt.geom.Point2D.Double(
                                Double.parseDouble(n.getAttribute("x")),
                                Double.parseDouble(n.getAttribute("y")));
                        break;
                    case "p3":
                        cp1 =
                            new java.awt.geom.Point2D.Double(
                                Double.parseDouble(n.getAttribute("x")),
                                Double.parseDouble(n.getAttribute("y")));
                        break;
                }
            }
        }

        return new java.awt.geom.QuadCurve2D.Double(
                startPoint.getX(), startPoint.getY(), cp1.getX(), cp1.getY(), endPoint.getX(), endPoint.getY());
    }

    public static void saveJPEG(BufferedImage image, String filename) {
        try (FileOutputStream imageFile =
                new FileOutputStream(filename.endsWith(".jpg") ? filename : filename + ".jpg")) {
            ImageIO.write(image, "jpeg", imageFile);
        } catch (IOException e) {
            LOGGER.atError().addArgument(e).log("Failed to save image.");
        }
    }
}
