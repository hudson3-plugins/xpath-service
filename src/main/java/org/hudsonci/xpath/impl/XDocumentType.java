/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hudsonci.xpath.impl;

import org.w3c.dom.*;

/**
 *
 * @author Bob Foster
 */
public class XDocumentType implements DocumentType {

  org.dom4j.DocumentType docType;
  Node parent;
  
  public XDocumentType(org.dom4j.DocumentType node, Node parent) {
    docType = node;
    this.parent = parent;
  }
  
  @Override
  public String getTextContent() {
    return null;
  }

  @Override
  public String getNodeName() {
    return getName();
  }

  @Override
  public String getNodeValue() {
    return null;
  }

  public String getName() {
    return docType.getElementName();
  }

  public String getPublicId() {
    return docType.getPublicID();
  }

  public String getSystemId() {
    return docType.getSystemID();
  }

  public NamedNodeMap getEntities() {
    return XNamedNodeMap.EMPTY;
  }

  public NamedNodeMap getNotations() {
    return XNamedNodeMap.EMPTY;
  }

  public String getInternalSubset() {
    return docType.asXML();
  }
  
  boolean isEq(String s1, String s2) {
    if (s1 == s2) return true;
    if (s1 == null || s2 == null) return false;
    return s1.equals(s2);
  }

  @Override
  public boolean isEqualNode(Node node) {
    if (node.getNodeType() != Node.DOCUMENT_TYPE_NODE) return false;
    DocumentType other = (DocumentType) node;
    if (!isEq(getPublicId(), other.getPublicId())) return false;
    if (!isEq(getSystemId(), other.getSystemId())) return false;
    NamedNodeMap thisEnts = getEntities();
    NamedNodeMap nodeEnts = other.getEntities();
    int len = thisEnts.getLength();
    if (len != nodeEnts.getLength()) return false;
    for (int i = 0; i < len; i++) {
      if (!thisEnts.item(i).isEqualNode(nodeEnts.item(i)))
        return false;
    }
    return true;
  }

  public void setNodeValue(String string) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public short getNodeType() {
    return Node.DOCUMENT_TYPE_NODE;
  }

  public Node getParentNode() {
    return parent;
  }
  
  private static NodeList EMPTY = new NodeList() {

    public Node item(int i) {
      throw new IndexOutOfBoundsException(""+i);
    }

    public int getLength() {
      return 0;
    }
  };

  public NodeList getChildNodes() {
    return EMPTY;
  }

  public Node getFirstChild() {
    return null;
  }

  public Node getLastChild() {
    return null;
  }

  public Node getPreviousSibling() {
    NodeList list = parent.getChildNodes();
    boolean found = false;
    for (int i = list.getLength() - 1; i >= 0; i--) {
      Node node = list.item(i);
      if (found)
        return node;
      found = node == this;
    }
    return null;
  }

  public Node getNextSibling() {
    NodeList list = parent.getChildNodes();
    boolean found = false;
    for (int i = 0, n = list.getLength(); i < n; i++) {
      Node node = list.item(i);
      if (found)
        return node;
      found = node == this;
    }
    return null;
  }
  
  private static final NamedNodeMap EMPTY_MAP = new NamedNodeMap() {

    public Node getNamedItem(String string) {
      return null;
    }

    public Node setNamedItem(Node node) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node removeNamedItem(String string) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node item(int i) {
      throw new IndexOutOfBoundsException(""+i);
    }

    public int getLength() {
      return 0;
    }

    public Node getNamedItemNS(String string, String string1) throws DOMException {
      return null;
    }

    public Node setNamedItemNS(Node node) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node removeNamedItemNS(String string, String string1) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
  };

  public NamedNodeMap getAttributes() {
    return EMPTY_MAP;
  }

  public Document getOwnerDocument() {
    return parent.getOwnerDocument();
  }

  public Node insertBefore(Node node, Node node1) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node replaceChild(Node node, Node node1) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node removeChild(Node node) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node appendChild(Node node) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean hasChildNodes() {
    return false;
  }

  public Node cloneNode(boolean bln) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void normalize() {
  }

  public boolean isSupported(String string, String string1) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getNamespaceURI() {
    return null;
  }

  public String getPrefix() {
    return null;
  }

  public void setPrefix(String string) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getLocalName() {
    return null;
  }

  public boolean hasAttributes() {
    return false;
  }

  public String getBaseURI() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  private int docPos;
  private int curPos;
  
  public int getDocumentPosition(Node parent, Node node) {
    if (parent == node)
      return curPos;
    NodeList children = parent.getChildNodes();
    for (int i = 0, n = children.getLength(); i < n; i++) {
      curPos++;
      int found = getDocumentPosition(children.item(i), node);
      if (found > 0)
        return found;
    }
    return 0;
  }
  
  public int getDocumentPosition(Node node) {
    curPos = 1;
    return getDocumentPosition(getOwnerDocument(), node);
  }

  public short compareDocumentPosition(Node node) throws DOMException {
    Document doc = getOwnerDocument();
    if (docPos == 0)
      docPos = getDocumentPosition(this);
    int pos = getDocumentPosition(node);
    return (short)(docPos < pos ? -1 : (docPos > pos ? 1 : 0));
  }

  public void setTextContent(String string) throws DOMException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isSameNode(Node node) {
    return this == node;
  }

  public String lookupPrefix(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isDefaultNamespace(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String lookupNamespaceURI(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object getFeature(String string, String string1) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object setUserData(String string, Object o, UserDataHandler udh) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object getUserData(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
