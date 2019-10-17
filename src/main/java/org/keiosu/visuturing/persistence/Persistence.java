package org.keiosu.visuturing.persistence;

import org.keiosu.visuturing.core.State;
import org.keiosu.visuturing.core.Symbols;
import org.keiosu.visuturing.core.Transition;
import org.keiosu.visuturing.core.TuringMachine;
import org.keiosu.visuturing.xml.XMLTransformer;
import org.keiosu.visuturing.xml.XmlElement;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.io.IOException;

import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Persistence {
  public Persistence() {
  }

  public static String trimExtension(String var0) {
    int var1 = var0.indexOf(".");
    return var1 < 0 ? var0 : var0.substring(0, var1);
  }

  public static void exportToHTML(XmlElement var0, String var1) throws IOException, TransformerException, TransformerConfigurationException {
    XMLTransformer var2 = new XMLTransformer("web/tm.xsl");
    String var3 = new String(var1);
    if (!var3.endsWith("html") && !var3.endsWith("htm")) {
      var3 = var3 + ".html";
    }

    File var4 = new File(var3);
    if (!var4.exists()) {
      var4.createNewFile();
    }

    FileOutputStream var5 = new FileOutputStream(var4);
    var2.transform(var0, var5);
    var5.close();
  }

  public static void save(XmlElement var0, String var1) throws IOException {
    save(var0, new File(var1));
  }

  public static void save(XmlElement var0, File var1) throws IOException {
    String var2 = trimExtension(var1.toString());
    BufferedWriter var3 = new BufferedWriter(new FileWriter(var2.concat(".vt")));
    String var4 = "<?xml version=\"1.0\" ?>\n\n<?author name='Brian L A Corbin' ?>\n<?program name='tBIT VisuTuring' version='1.0 3YP' ?>";
    var3.write(var4 + var0.toXml());
    var3.close();
  }

  public static TuringMachine load(File var0) throws SAXException, Exception {
    DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
    DocumentBuilder var2 = var1.newDocumentBuilder();
    Document var3 = var2.parse(var0);
    Element var4 = var3.getDocumentElement();
    return retrieveTuringMachine(var4);
  }

  private static TuringMachine retrieveTuringMachine(Element var0) {
    String var1 = "<no-name>";
    String var2 = "none";
    boolean var3 = false;
    Vector var4 = new Vector();
    Vector var5 = new Vector();
    Vector var6 = new Vector();
    NodeList var7 = var0.getChildNodes();

    for(int var8 = 0; var8 < var7.getLength(); ++var8) {
      Node var9 = var7.item(var8);
      if (var9 instanceof Element) {
        Element var10 = (Element)var9;
        Text var11 = (Text)var10.getFirstChild();
        if (var10.getTagName().equals("name")) {
          var1 = var11.getData();
        } else if (var10.getTagName().equals("description")) {
          var2 = var11.getData();
        } else if (var10.getTagName().equals("has-diagram")) {
          var3 = var11.getData().equals("true");
        } else if (var10.getTagName().equals("states")) {
          var4 = getStates(var10);
        } else if (var10.getTagName().equals("alphabet")) {
          var5 = getAlphabet(var10);
        } else if (var10.getTagName().equals("transitions")) {
          var6 = getTransitions(var10);
        }
      }
    }

    TuringMachine var12 = new TuringMachine(var1, var2, var6, var5, var4);
    var12.setHasDiagram(var3);
    return var12;
  }

  private static Vector getStates(Element var0) {
    Vector var1 = new Vector();
    NodeList var2 = var0.getChildNodes();

    for(int var3 = 0; var3 < var2.getLength(); ++var3) {
      Node var4 = var2.item(var3);
      if (var4 instanceof Element) {
        String var5 = "";
        double var6 = (double)((var3 + 1) * 50);
        double var8 = 50.0D;
        Element var10 = (Element)var4;
        if (var10.getTagName().equals("state")) {
          NodeList var11 = var10.getChildNodes();

          for(int var12 = 0; var12 < var11.getLength(); ++var12) {
            Node var13 = var11.item(var12);
            if (var13 instanceof Element) {
              Element var14 = (Element)var13;
              Text var15 = (Text)var14.getFirstChild();
              if (var14.getTagName().equals("name")) {
                var5 = var15.getData();
              } else if (var14.getTagName().equals("location")) {
                var6 = Double.parseDouble(var14.getAttribute("x"));
                var8 = Double.parseDouble(var14.getAttribute("y"));
              }
            }
          }

          var1.add(new State(var5, new Point((int)var6, (int)var8)));
        }
      }
    }

    return var1;
  }

  private static Vector getAlphabet(Element var0) {
    Vector var1 = new Vector();
    NodeList var2 = var0.getChildNodes();

    for(int var3 = 0; var3 < var2.getLength(); ++var3) {
      Node var4 = var2.item(var3);
      if (var4 instanceof Element) {
        Element var5 = (Element)var4;
        Text var6 = (Text)var5.getFirstChild();
        if (var5.getTagName().equals("symbol")) {
          var1.add("" + Symbols.unicodeToChar(var6.getData()));
        }
      }
    }

    return var1;
  }

  private static Vector getTransitions(Element var0) {
    Vector var1 = new Vector();
    NodeList var2 = var0.getChildNodes();

    for(int var3 = 0; var3 < var2.getLength(); ++var3) {
      String var4 = "";
      String var5 = "";
      char var6 = ' ';
      char var7 = ' ';
      Object var8 = new java.awt.geom.QuadCurve2D.Double();
      Node var9 = var2.item(var3);
      if (var9 instanceof Element) {
        Element var10 = (Element)var9;
        if (var10.getTagName().equals("transition")) {
          NodeList var11 = var10.getChildNodes();

          for(int var12 = 0; var12 < var11.getLength(); ++var12) {
            Node var13 = var11.item(var12);
            if (var13 instanceof Element) {
              Element var14 = (Element)var13;
              Text var15 = (Text)var14.getFirstChild();
              if (var14.getTagName().equals("current-state")) {
                var4 = var15.getData();
              } else {
                char var16;
                if (var14.getTagName().equals("current-symbol")) {
                  var16 = Symbols.unicodeToChar(var15.getData());
                  var6 = var16;
                } else if (var14.getTagName().equals("next-state")) {
                  var5 = var15.getData();
                } else if (var14.getTagName().equals("task")) {
                  var16 = Symbols.unicodeToChar(var15.getData());
                  var7 = var16;
                } else if (var14.getTagName().equals("edge")) {
                  var8 = getEdge(var14);
                }
              }
            }
          }

          Transition var17 = new Transition(var4, var6, var5, var7);
          var17.setEdge((QuadCurve2D)var8);
          var1.add(var17);
        }
      }
    }

    return var1;
  }

  private static QuadCurve2D getEdge(Element var0) {
    java.awt.geom.Point2D.Double var1 = new java.awt.geom.Point2D.Double();
    java.awt.geom.Point2D.Double var2 = new java.awt.geom.Point2D.Double();
    java.awt.geom.Point2D.Double var3 = new java.awt.geom.Point2D.Double();
    NodeList var4 = var0.getChildNodes();

    for(int var5 = 0; var5 < var4.getLength(); ++var5) {
      Node var6 = var4.item(var5);
      if (var6 instanceof Element) {
        Element var7 = (Element)var6;
        Text var8 = (Text)var7.getFirstChild();
        if (var7.getTagName().equals("p1")) {
          var1 = new java.awt.geom.Point2D.Double(Double.parseDouble(var7.getAttribute("x")), Double.parseDouble(var7.getAttribute("y")));
        } else if (var7.getTagName().equals("p2")) {
          var2 = new java.awt.geom.Point2D.Double(Double.parseDouble(var7.getAttribute("x")), Double.parseDouble(var7.getAttribute("y")));
        } else if (var7.getTagName().equals("p3")) {
          var3 = new java.awt.geom.Point2D.Double(Double.parseDouble(var7.getAttribute("x")), Double.parseDouble(var7.getAttribute("y")));
        }
      }
    }

    return new java.awt.geom.QuadCurve2D.Double(var1.getX(), var1.getY(), var3.getX(), var3.getY(), var2.getX(), var2.getY());
  }

  public static void saveJPEG(BufferedImage var0, String var1, float var2) {
    try {
      if (!var1.endsWith(".jpg")) {
        var1 = var1 + ".jpg";
      }

      FileOutputStream var3 = new FileOutputStream(var1);
      ImageIO.write(var0, "jpeg", var3);
      var3.close();

    } catch (IOException var6) {
      System.out.println("IO Error on output: " + var6);
    }

  }
}
