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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.dom4j.Document;
import org.hudsonci.xpath.impl.Dom2Dom;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Bob Foster
 */
public class TestXPathOnXNode extends TestDocumentSupport {

  Document doc;
  org.w3c.dom.Document xdoc;
  
  private void getDocuments(String resourceName, boolean validate) {
    doc = getDom4jDocument(resourceName, false);
    try {
      xdoc = (org.w3c.dom.Document) new Dom2Dom().dom2Dom(doc, validate);
    } catch (XPathException ex) {
      fail("Can't get mapped document "+ex);
    }
  }
  
  public void testNodeListXPathOnXNode() {
    getDocuments("test1.xml", false);
    XPath xpath = getXPath();
    try {
      Object obj = xpath.evaluate("/root/children/child", xdoc, XPathConstants.NODESET);
      if (obj instanceof NodeList) {
        NodeList list = (NodeList) obj;
        int num = list.getLength();
        assertTrue("NodeList has 2 nodes", num == 2);
        Node node1 = list.item(0);
        Node node2 = list.item(1);
        String name1 = node1.getNodeName();
        String name2 = node2.getNodeName();
        assertTrue("result is 2 child nodes", "child".equals(name1) && "child".equals(name2));
        org.w3c.dom.Document isDoc = node1.getOwnerDocument();
        assertTrue("node belongs to document", xdoc == isDoc);
      }
    } catch (XPathExpressionException ex) {
      fail("/root/children/child not accepted "+ex);
    }
  }
  
  public void testNodeXPathOnXNode() {
    getDocuments("test1.xml", false);
    XPath xpath = getXPath();
    try {
      Object obj = xpath.evaluate("/root/children/child[@id=\"baz\"]", xdoc, XPathConstants.NODESET);
      if (obj instanceof NodeList) {
        NodeList list = (NodeList) obj;
        int num = list.getLength();
        assertTrue("NodeList has 1 node", num == 1);
        Node node1 = list.item(0);
        assertTrue("node is Node", node1 instanceof org.w3c.dom.Node);
        String name1 = node1.getNodeName();
        assertTrue("result is child node", "child".equals(name1));
      }
    } catch (XPathExpressionException ex) {
      fail("/root/children/child[@id=\"baz\"] not accepted "+ex);
    }
  }
  
  public void testStringXPathOnXNode() {
    getDocuments("test1.xml", false);
    XPath xpath = getXPath();
    try {
      Object obj = xpath.evaluate("/root/children/bool", xdoc, XPathConstants.STRING);
      assertTrue("object is String", obj instanceof String);
      assertTrue("string is \"true\"", "true".equals(obj));
    } catch (XPathExpressionException ex) {
      fail("/root/children/bool not accepted "+ex);
    }
  }
  
  public void testNumberXPathOnXNode() {
    getDocuments("test1.xml", false);
    XPath xpath = getXPath();
    try {
      Object obj = xpath.evaluate("/root/children/num", xdoc, XPathConstants.NUMBER);
      assertTrue("result is Double", obj instanceof Double);
      assertTrue("result is 2.0", (Double)obj == 2.0D);
    } catch (XPathExpressionException ex) {
      fail("/root/children/num not accepted "+ex);
    }
  }

  public void testBooleanXPathOnXNode() {
    getDocuments("test1.xml", false);
    XPath xpath = getXPath();
    try {
      Object obj = xpath.evaluate("/root/children/bool", xdoc, XPathConstants.BOOLEAN);
      assertTrue("result is Boolean", obj instanceof Boolean);
      assertTrue("result is true", (Boolean) obj);
    } catch (XPathExpressionException ex) {
      fail("/root/children/bool not accepted "+ex);
    }
  }
}
