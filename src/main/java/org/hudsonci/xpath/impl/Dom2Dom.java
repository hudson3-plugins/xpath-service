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

package org.hudsonci.xpath.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.*;
import org.hudsonci.xpath.XPathException;
import org.w3c.dom.NodeList;

/**
 * Dom2Dom translates a dom4j document to a W3C document and keeps
 * a map of dom4j nodes translated to W3C nodes.
 * 
 * @author Bob Foster
 */
public class Dom2Dom {
  
  private static Map<Document,DocMapPair> cache
          = new WeakHashMap<Document,DocMapPair>();
  
  private ReverseMap reverseMap;
  private org.w3c.dom.Document wdoc;
  private boolean trimText;
  private org.w3c.dom.Element currentElement;
  private Text lastText;
  
  /* These classes for much-needed brevity */
  private static class ReverseMap extends HashMap<org.w3c.dom.Node,Node> {}
  private static class DocMapPair extends Pair<org.w3c.dom.Document,ReverseMap> {
    public DocMapPair(org.w3c.dom.Document doc,ReverseMap map) {
      super(doc, map);
    }
  }

  private String getLocalName(String qualifiedName) {
    // different representation in w3c
    String localName = null;
    int colon = qualifiedName.indexOf(':');
    if (colon >= 0)
      localName = qualifiedName.substring(colon+1);
    return localName;
  }
  
  private String getNamespaceURI(String namespaceURI) {
    // different representation in w3c
    if ("".equals(namespaceURI))
      return null;
    return namespaceURI;
  }
  
  private class Name {
    String namespaceURI;
    String qualifiedName;
    Name(Node n) {
      qualifiedName = n.getName();
    }
    Name(Element el) {
      qualifiedName = el.getQualifiedName();
      namespaceURI = getNamespaceURI(el.getNamespaceURI());
    }
    Name(Attribute el) {
      // thank dom4j for this code duplication
      qualifiedName = el.getQualifiedName();
      namespaceURI = getNamespaceURI(el.getNamespaceURI());
    }
    Name(Namespace ns) {
      String prefix = ns.getPrefix();
      qualifiedName = "xmlns" + ("".equals(prefix) ? "" : ":"+prefix);
    }
  }
  
  /**
   * Get original dom4j Node. Valid if dom2dom returned successfully.
   * 
   * @see #dom2Dom(org.dom4j.Node, boolean)
   * 
   * @param node W3C DOM Node
   * @return the dom4j node that was mapped by dom2dom to the argument node
   */
  public Node getOriginalNode(org.w3c.dom.Node node) {
    Node d4jNode = reverseMap.get(node);
    if (d4jNode == null) {
      // Xerces is playing tricks, allocating nodes on the fly.
      SimplePath path = getPathForNode(node, null);
      Document doc = getDom4jDocument(node);
      d4jNode = getNodeForPath(doc, path);
    }
    return d4jNode;
  }
  
  private Document getDom4jDocument(org.w3c.dom.Node node) {
    org.w3c.dom.Document doc = node.getOwnerDocument();
    Document document = (Document) reverseMap.get(doc);
    if (document == null)
      throw new IllegalStateException("Document not found in reverse map");
    return document;
  }
  
  private static class SimplePath {
    int childNum;
    SimplePath next;
    SimplePath(int childNum, SimplePath next) {
      this.childNum = childNum;
      this.next = next;
    }
  }
  
  private SimplePath getPathForNode(org.w3c.dom.Node node, SimplePath next) {
    if (node instanceof org.w3c.dom.Document)
      return next;
    org.w3c.dom.Node parent = node.getParentNode();
    int i = 0;
    for (org.w3c.dom.Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.equals(node))
        return getPathForNode(parent, new SimplePath(i, next));
      i++;
    }
    throw new IllegalStateException("Node not a child of its parent");
  }
  
  private Node getNodeForPath(Document doc, SimplePath path) {
    Node node = doc;
    for (SimplePath p = path; p != null; p = p.next) {
      if (!(node instanceof Branch))
        throw new IllegalStateException("Node with children not a Branch");
      Branch parent = (Branch) node;
      node = parent.node(p.childNum);
    }
    return node;
  }
  
  /**
   * Map a org.dom4j.Node to a org.w3c.dom.Node, including its ancestors
   * and descendents.
   * 
   * Once mapped, originalNode will return the original Node
   * corresponding to any mapped w3c Node.
   * 
   * @see #getOriginalNode(org.w3c.dom.Node)
   * 
   * @param node to convert
   * @param trim true if whitespace is to be trimmed from any consecutive
   * sequence of Text nodes; false if whitespace retained
   * @return W3C DOM Node corresponding to node argument
   * @throws XPathException 
   */
  public org.w3c.dom.Node dom2DomX(Node node, boolean trim) throws XPathException {
    
    trimText = trim;
    
    // The first time we see a context node we create a w3c Document
    // for XPath processing. The important thing is that equals
    // works correctly.
    
    Document ddoc = node.getDocument();
    
    reverseMap = new ReverseMap();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
      throw new XPathException(ex);
    }
    wdoc = builder.newDocument();

    createChildren(ddoc, wdoc);
        
    return findNode(wdoc, node);
  }
  
  public org.w3c.dom.Node dom2Dom(Node node, boolean trim) throws XPathException {
    
    trimText = trim;
    
    // The first time we see a context node we create a w3c Document
    // for XPath processing. The important thing is that equals
    // works correctly.
    
    // This cache is probably bogus because Xerces likes to create Nodes
    // as flyweight objects on the fly. So different operations might
    // retrieve the same actual Node in two different NodeImpl objects
    // that do not compare equals.
    //
    // It did seem to work for some simple tests and when it does work
    // it is fast, but I've worked around it in getOriginalNode.
    
    Document ddoc = node.getDocument();
    DocMapPair pair = cache.get(ddoc);
    
    if (pair == null) {
      
      ReverseMap map = new ReverseMap();
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder;
      try {
        builder = factory.newDocumentBuilder();
      } catch (ParserConfigurationException ex) {
        throw new XPathException(ex);
      }
      org.w3c.dom.Document doc = builder.newDocument();
      
      map.put(doc, ddoc);
      pair = new DocMapPair(doc, map);
      
      cache.put(ddoc, pair);
    }
    
    wdoc = pair.getLeft();
    reverseMap = pair.getRight();
    
    if (!wdoc.hasChildNodes())
      createChildren(ddoc, wdoc);
        
    return findNode(wdoc, node);
  }
  
  private org.w3c.dom.Node findNode(org.w3c.dom.Node ancestor, Node node) {
    Node ancestorNode = reverseMap.get(ancestor);
    if (ancestorNode == node)
      return ancestor;
    NodeList list = ancestor.getChildNodes();
    for (int i = 0, n = list.getLength(); i < n; i++) {
      org.w3c.dom.Node child = (org.w3c.dom.Node) list.item(i);
      org.w3c.dom.Node found = findNode(child, node);
      if (found != null)
        return found;
    }
    return null;
  }
  
  private void createChildren(Branch dparent, org.w3c.dom.Node wparent) {
      Branch b = (Branch) dparent;
      for (int i = 0, n = b.nodeCount(); i < n; i++) {
        Node child = b.node(i);
        org.w3c.dom.Node wchild = createChild(child, wparent);
        if (wchild != null)
          createChildren((Branch)child, wchild);
      }
      endText(wparent);
  }
  
  StringBuilder textBuilder = new StringBuilder();
  
  private void endText(org.w3c.dom.Node wparent) {
    if (textBuilder.length() > 0) {
      String text = trimText ? textBuilder.toString().trim() : textBuilder.toString();
      org.w3c.dom.Text textNode = wdoc.createTextNode(text);
      wparent.appendChild(textNode);
      textBuilder.setLength(0);
      reverseMap.put(textNode, lastText);
    }
  }
  
  private org.w3c.dom.Node createChild(Node child, org.w3c.dom.Node wparent) {
    int type = child.getNodeType();
    
    // Collapse multiple consecutive text nodes to a single text node
    // with trimmed value.
    if (type != Node.TEXT_NODE)
      endText(wparent);
    
    Name name;
    org.w3c.dom.Node node = null;
    
    switch (type) {
      case Node.ATTRIBUTE_NODE:
        break;
      case Node.CDATA_SECTION_NODE:
        CDATA cd = (CDATA) child;
        wparent.appendChild(node = wdoc.createCDATASection(cd.getText()));
        break;
      case Node.COMMENT_NODE:
        Comment co = (Comment) child;
        wparent.appendChild(node = wdoc.createComment(co.getText()));
        break;
      case Node.DOCUMENT_TYPE_NODE:
        DocumentType dt = (DocumentType) child;
        wparent.appendChild(new XDocumentType(dt, wparent));
        break;
      case Node.ELEMENT_NODE:
        Element el = (Element) child;
        name = new Name(el);
        org.w3c.dom.Element e = name.namespaceURI == null
                ? wdoc.createElement(name.qualifiedName)
                : wdoc.createElementNS(name.namespaceURI, name.qualifiedName);
        wparent.appendChild(e);
        node = currentElement = e;
        
        for (int i = 0, n = el.attributeCount(); i < n; i++) {
          Attribute at = el.attribute(i);
          name = new Name(at);
          if (name.namespaceURI == null)
            e.setAttribute(name.qualifiedName, at.getValue());
          else
            e.setAttributeNS(name.namespaceURI, name.qualifiedName, at.getValue());
        }
        return e;
      case Node.ENTITY_REFERENCE_NODE:
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        ProcessingInstruction p = (ProcessingInstruction) child;
        wparent.appendChild(node = wdoc.createProcessingInstruction(p.getTarget(), p.getText()));
        break;
      case Node.TEXT_NODE:
        textBuilder.append(child.getText());
        lastText = (Text) child;
        break;
      case Node.NAMESPACE_NODE:
        Namespace ns = (Namespace) child;
        name = new Name(ns);
        currentElement.setAttribute(name.qualifiedName, ns.getURI());
        break;
      default:
        throw new IllegalStateException("Unknown node type");
    }
    if (node != null)
      reverseMap.put(node, child);
    return null;
  }
}
