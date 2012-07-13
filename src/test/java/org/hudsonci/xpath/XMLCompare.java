/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hudsonci.xpath;

import org.w3c.dom.*;

/**
 * Emulates W3C DOM Level 3 isEqualNode, with added output for debugging.
 * Used to determine why nodes are not equal.
 * 
 * @author Bob Foster
 */
public class XMLCompare {
  
  private static final boolean DEBUG = true;
  
  /**
   * Compare two nodes according to the  W3C DOM Level 3 specification.
   * 
   * @see http://www.w3.org/TR/DOM-Level-3-Core/core.html#Node3-isEqualNode
   * 
   * The 'self' variable takes the role that 'this'
   * plays in the OOP version.
   * 
   * NB: This does NOT give the same results as the javax implementation!
   * Some documents succeed here that fail under isEqualNode. In all
   * cases we've seen, when those documents are printed with an XSLT
   * null transform, they are identical.
   * 
   * Obviously, either Xerces applies some test not or not according to the
   * spec or this class doesn't implement the spec correctly. In any case,
   * this test is a decent indicator that the two classes will produce
   * equivalent results when queried with XPath, so it is preferred.
   * 
   * @param self
   * @param node
   * @return true iff nodes are equal
   */
  public static boolean isEqual(Node self, Node node) {
    if (self instanceof Element)
      return isEqualElement(self, node);
    else if (self instanceof DocumentType)
      return isEqualDocumentType(self, node);
    return isEqualNode(self, node);
  }
  
  //-------------------- DocumentType ------------------------------

  public static boolean isEqualDocumentType(Node s, Node node) {
    if (node.getNodeType() != Node.DOCUMENT_TYPE_NODE)
      return false;
    DocumentType self  = (DocumentType) s;
    DocumentType other = (DocumentType) node;
    if (!isEq(self.getPublicId(), other.getPublicId()))
      return false;
    if (!isEq(self.getSystemId(), other.getSystemId()))
      return false;
    NamedNodeMap thisEnts = self.getEntities();
    NamedNodeMap nodeEnts = other.getEntities();
    int len = thisEnts.getLength();
    if (len != nodeEnts.getLength())
      return false;
    for (int i = 0; i < len; i++) {
      if (!isEqualNode(thisEnts.item(i), nodeEnts.item(i)))
        return false;
    }
    return isEqualNode(self, node);
  }
  
  //-------------------- Element -----------------------------------

  private static void debugDumpAttrs(String what, NamedNodeMap attrs) {
    System.out.println("** "+what+" attrs:");
    int len = attrs.getLength();
    for (int i = 0; i < len; i++) {
      Attr attr = (Attr) attrs.item(i);
      debugPrintNode(attr);
    }
  }
  
  private static boolean debugDumpAttrs(Node self, Node node) {
    NamedNodeMap thisAttrs = self.getAttributes();
    debugDumpAttrs("this", thisAttrs);
    Element other = (Element) node;
    NamedNodeMap nodeAttrs = node.getAttributes();
    debugDumpAttrs("node", nodeAttrs);
    return false;
  }
  
  private static boolean debugIsEqualElement(Node self, Node node) {
    if (node.getNodeType() != Node.ELEMENT_NODE)
      return debugPrintNode(self) && debugPrintNode(node);
    Element other = (Element) node;
    NamedNodeMap thisAttrs = self.getAttributes();
    NamedNodeMap nodeAttrs = node.getAttributes();
    int len = thisAttrs.getLength();
    int nlen = nodeAttrs.getLength();
    if (len != nlen)
      return debugDumpAttrs(self, node);
    for (int i = 0; i < len; i++) {
      Attr thisAttr = (Attr) thisAttrs.item(i);
      Attr nodeAttr = null;
      if (thisAttr.getLocalName() == null)
        nodeAttr = (Attr) nodeAttrs.getNamedItem(thisAttr.getNodeName());
      else
        nodeAttr = (Attr) nodeAttrs.getNamedItemNS(thisAttr.getNamespaceURI(), thisAttr.getLocalName());
      if (nodeAttr == null)
        return debugDumpAttrs(self, node);
      if (!thisAttr.isEqualNode(nodeAttr))
        return false;
    }
    return isEqualNode(self, node);
  }

  private static boolean isEqualElement(Node self, Node node) {
    if (DEBUG) return debugIsEqualElement(self, node);
    else {
      if (node.getNodeType() != Node.ELEMENT_NODE) return false;
      Element other = (Element) node;
      NamedNodeMap thisAttrs = self.getAttributes();
      NamedNodeMap nodeAttrs = node.getAttributes();
      int len = thisAttrs.getLength();
      int nlen = nodeAttrs.getLength();
      if (len != nlen) return false;
      for (int i = 0; i < len; i++) {
        Attr thisAttr = (Attr) thisAttrs.item(i);
        Attr nodeAttr = null;
        if (thisAttr.getLocalName() == null)
          nodeAttr = (Attr) nodeAttrs.getNamedItem(thisAttr.getNodeName());
        else
          nodeAttr = (Attr) nodeAttrs.getNamedItemNS(thisAttr.getNamespaceURI(), thisAttr.getLocalName());
        if (nodeAttr == null) return false;
        if (!thisAttr.isEqualNode(nodeAttr)) return false;
      }
      return isEqualNode(self, node);
    }
  }

  //-------------------- Node --------------------------------------

  private static boolean isEq(String s1, String s2) {
    return s1 == s1 || (s1 != null && s2 != null && s1.equals(s2));
  }
  
  private static String debugNodeType(Node node) {
    switch (node.getNodeType()) {
      case Node.ATTRIBUTE_NODE:
        return "ATTRIBUTE_NODE";
      case Node.CDATA_SECTION_NODE:
        return "CDATA_SECTION_NODE";
      case Node.COMMENT_NODE:
        return "COMMENT_NODE";
      case Node.DOCUMENT_FRAGMENT_NODE:
        return "DOCUMENT_FRAGMENT_NODE";
      case Node.DOCUMENT_NODE:
        return "DOCUMENT_NODE";
      case Node.DOCUMENT_TYPE_NODE:
        return "DOCUMENT_TYPE_NODE";
      case Node.ELEMENT_NODE:
        return "ELEMENT_NODE";
      case Node.ENTITY_NODE:
        return "ENTITY_NODE";
      case Node.ENTITY_REFERENCE_NODE:
        return "ENTITY_REFERENCE_NODE";
      case Node.NOTATION_NODE:
        return "NOTATION_NODE";
      case Node.PROCESSING_INSTRUCTION_NODE:
        return "PROCESSING_INSTRUCTION_NODE";
      case Node.TEXT_NODE:
        return "TEXT_NODE";
      default:
        return "UNKNOWN";
    }
  }
  
  private static boolean debugPrintNode(Node node) {
    System.out.println("");
    System.out.println("  nodeType="+debugNodeType(node));
    System.out.println("  nodeName=\""+node.getNodeName()+"\"");
    System.out.println("  nodeValue=\""+node.getNodeValue()+"\"");
    System.out.println("  prefix=\""+node.getPrefix()+"\"");
    System.out.println("  localName=\""+node.getLocalName()+"\"");
    System.out.println("  namespaceURI=\""+node.getNamespaceURI()+"\"");
    System.out.println("  numChildren="+node.getChildNodes().getLength());
    return false;
  }
  
  private static void debugPrintChildTypes(Node node) {
    NodeList nodeChildren = node.getChildNodes();
    for (int i = 0, n = nodeChildren.getLength(); i < n; i++) {
      Node child = nodeChildren.item(i);
      System.out.println("  child["+i+"] is "+debugNodeType(child)+" "+child.getNodeName());
    }
  }
  
  private static boolean debugIsEqualNode(Node self, Node node) {
    short thisType = self.getNodeType();;
    short nodeType = node.getNodeType();
    if (thisType != nodeType)
      return debugPrintNode(self) && debugPrintNode(node);
    String thisVal = self.getPrefix();
    String nodeVal = node.getPrefix();
    if (!isEq(thisVal, nodeVal))
      return debugPrintNode(self) && debugPrintNode(node);
    thisVal = self.getNodeName();
    nodeVal = node.getNodeName();
    if (!isEq(thisVal, nodeVal))
      return debugPrintNode(self) && debugPrintNode(node);
    thisVal = self.getNodeValue();
    nodeVal = node.getNodeValue();
    if (!isEq(thisVal, nodeVal))
      return debugPrintNode(self) && debugPrintNode(node);
    thisVal = self.getLocalName();
    nodeVal = node.getLocalName();
    if (!isEq(thisVal, nodeVal))
      return false;
    thisVal = self.getNamespaceURI();
    nodeVal = node.getNamespaceURI();
    if (!isEq(thisVal, nodeVal))
      return debugPrintNode(self) && debugPrintNode(node);
    NodeList thisChildren = self.getChildNodes();
    NodeList nodeChildren = node.getChildNodes();
    int len = thisChildren.getLength();
    int nlen = nodeChildren.getLength();
    if (len != nlen) {
      debugPrintNode(self);
      debugPrintChildTypes(self);
      debugPrintNode(node);
      debugPrintChildTypes(node);
      return false;
    }
    for (int i = 0; i < len; i++) {
      if (!isEqualNode(thisChildren.item(i), nodeChildren.item(i)))
        return false;
    }
    return true;
  }
  
  private static boolean isEqualNode(Node self, Node node) {
    if (!DEBUG) {
    
    if (self.getNodeType() != node.getNodeType()) return false;
    if (!isEq(self.getPrefix(), node.getPrefix())) return false;
    if (!isEq(self.getNodeName(), node.getNodeName())) return false;
    if (!isEq(self.getNodeValue(), node.getNodeValue())) return false;
    if (!isEq(self.getLocalName(), node.getLocalName())) return false;
    if (!isEq(self.getNamespaceURI(), node.getNamespaceURI())) return false;
    NodeList thisChildren = self.getChildNodes();
    NodeList nodeChildren = node.getChildNodes();
    int len = thisChildren.getLength();
    int nlen = nodeChildren.getLength();
    if (len != nlen) return false;
    for (int i = 0; i < len; i++) {
      if (!thisChildren.item(i).isEqualNode(nodeChildren.item(i)))
        return false;
    }
    return true;
    
    } else
      return debugIsEqualNode(self, node);
  }

}
