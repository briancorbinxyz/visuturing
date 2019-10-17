package org.keiosu.visuturing.xml;

import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLTransformer {
  Transformer stylesheet;
  TransformerFactory tFactory = TransformerFactory.newInstance();

  public XMLTransformer(String var1) throws TransformerConfigurationException {
    this.stylesheet = this.tFactory.newTransformer(new StreamSource(new File(var1)));
  }

  public void transform(XmlElement var1, OutputStream var2) throws TransformerException {
    String var3 = "<?xml version=\"1.0\" ?>\n\n<?author name='Brian L A Corbin' ?>\n<?program name='tBIT VisuTuring' version='1.0 3YP' ?>";
    this.stylesheet.transform(new StreamSource(new StringReader(var3 + "\n\n" + var1.toXml())), new StreamResult(var2));
  }
}
