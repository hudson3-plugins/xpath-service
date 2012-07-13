package org.hudsonci.xpath;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A class that creates parsers like Jelly does.
 *
 * @author Bob Foster
 */
public class XMLParser {

  SAXParser parser;
  SAXParserFactory factory;
  XMLReader reader;
  public static final XMLParser instance = new XMLParser();

  private XMLParser() {
  }

  public static XMLParser getXMLParser() {
    return instance;
  }

  public SAXParser getParser(boolean validating) {
    // Return the parser we already created (if any)
    if (parser != null) {
      return parser;
    }
    try {
      if (factory == null)
        factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setValidating(validating);
      parser = factory.newSAXParser();
      return (parser);
    } catch (Exception e) {
      System.out.println("Cannot get SAXParser " + e);
      return null;
    }
  }

  public XMLReader getReader(boolean validating) {
    try {
      return (getXMLReader(validating));
    } catch (SAXException e) {
      System.out.println("Cannot get XMLReader " + e);
      return null;
    }
  }

  public synchronized XMLReader getXMLReader(boolean validating) throws SAXException {
    if (reader == null) {
      reader = getParser(validating).getXMLReader();
      reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
    }
    return reader;
  }
}
