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

public class XmlTransformer {
    private final Transformer stylesheet;

    public XmlTransformer(String stylesheetFile) throws TransformerConfigurationException {
        this.stylesheet =
                TransformerFactory.newDefaultInstance()
                        .newTransformer(new StreamSource(new File(stylesheetFile)));
    }

    public void transform(XmlElement xmlElement, OutputStream outputStream)
            throws TransformerException {
        String xmlHeader = xmlHeader();
        this.stylesheet.transform(
                new StreamSource(new StringReader(xmlHeader + "\n\n" + xmlElement.toXml())),
                new StreamResult(outputStream));
    }

    private String xmlHeader() {
        return "<?xml version=\"1.0\" ?>\n\n" + "<?program name='VisuTuring' version='2.0.0' ?>";
    }
}
