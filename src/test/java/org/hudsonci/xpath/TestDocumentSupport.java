/*******************************************************************************
 *
 * Copyright (c) 2012 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *
 *    Bob Foster
 *     
 *******************************************************************************/ 

package org.hudsonci.xpath;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import junit.framework.TestCase;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Bob Foster
 */
public class TestDocumentSupport extends TestCase {

  public void printStdDocument(org.w3c.dom.Document doc) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);
      String xmlString = result.getWriter().toString();
      System.out.println(xmlString);
    } catch (TransformerException ex) {
      fail(ex.toString());
    }
  }
    
    public org.w3c.dom.Document getStdDocument(String resourceName, boolean validating) {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(validating);
      InputStream in = getClass().getResourceAsStream(resourceName);
      assertTrue("Resource "+resourceName+" found", in != null);
      try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(in);
      } catch (SAXException e) {
        fail(e.toString());
      } catch (ParserConfigurationException e) {
        fail(e.toString());
      } catch (IOException e) {
        fail(e.toString());
      } finally {
        try { in.close(); } catch (Exception e) {;}
      }
    return null;
  }
  
  public Document getDom4jDocument(String resourceName, boolean validating) {
    XMLReader xmlReader = XMLParser.getXMLParser().getReader(false);
    SAXReader reader = new SAXReader(xmlReader);
    InputStream in = getClass().getResourceAsStream(resourceName);
    assertTrue("Resource "+resourceName+" found", in != null);
    Document doc = null;

    try {
      doc = reader.read(in);
    } catch (DocumentException e) {
      fail("Can't read "+resourceName+" "+e);
    } finally {
      try { in.close(); } catch (Exception e) {;}
    }
    
    return doc;
  }

  public XPath getXPath() {
    XPathFactory factory = XPathFactory.newInstance();
    return factory.newXPath();
  }
  
  // So the "TestCase" contains at least one test
  public void testDummy() {}
  
  
  
}
