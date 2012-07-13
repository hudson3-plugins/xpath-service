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

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.dom4j.*;

/**
 * Implement dom4j Attribute for namespace attributes, which in
 * dom4j are sent as separate Namespace nodes after the element.
 * 
 * @author Bob Foster
 */
public class XNamespaceAttribute implements Attribute {

  String name;
  String value;
  Namespace ns;
  
  public XNamespaceAttribute(String prefix, String value) {
    this.name = "".equals(prefix) ? "xmlns" : "xmlns:"+prefix;
    this.value = value;
    this.ns = DocumentHelper.createNamespace("xmlns", "");
  }
  
  public Object clone() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  public QName getQName() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Namespace getNamespace() {
    return ns;
  }

  public void setNamespace(Namespace nmspc) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getNamespacePrefix() {
    return ns.getPrefix();
  }

  public String getNamespaceURI() {
    return "";
  }

  public String getQualifiedName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object getData() {
    return value;
  }

  public void setData(Object o) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean supportsParent() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Element getParent() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setParent(Element elmnt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Document getDocument() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setDocument(Document dcmnt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isReadOnly() {
    return true;
  }

  public boolean hasContent() {
    return true;
  }

  public String getName() {
    return name;
  }

  public void setName(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getText() {
    return value;
  }

  public void setText(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getStringValue() {
    return value;
  }

  public String getPath() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getPath(Element elmnt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getUniquePath() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getUniquePath(Element elmnt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String asXML() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void write(Writer writer) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public short getNodeType() {
    return Attribute.ATTRIBUTE_NODE;
  }

  public String getNodeTypeName() {
    return "ATTRIBUTE_NODE";
  }

  public Node detach() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List selectNodes(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Object selectObject(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List selectNodes(String string, String string1) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List selectNodes(String string, String string1, boolean bln) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node selectSingleNode(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String valueOf(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Number numberValueOf(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean matches(String string) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public org.dom4j.XPath createXPath(String string) throws InvalidXPathException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Node asXPathResult(Element elmnt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void accept(Visitor vstr) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
