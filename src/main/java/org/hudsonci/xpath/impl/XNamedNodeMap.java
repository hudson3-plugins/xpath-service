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
import java.util.List;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Bob Foster
 */
public class XNamedNodeMap implements NamedNodeMap {
  
  public static final XNamedNodeMap EMPTY = new XNamedNodeMap();
  
  private static class NSName {
    String namespaceURI;
    String localName;
    String name;
    NSName(String ns, String ln) {
      namespaceURI = ns;
      localName = ln;
    }
    NSName(String nm) {
      name = nm;
    }
    public boolean equals(Object obj) {
      if (!(obj instanceof NSName))
        return false;
      NSName other = (NSName) obj;
      return localName != null
        ? namespaceURI.equals(other.namespaceURI) && localName.equals(other.localName)
        : name.equals(other.name);
    }
    public int hashCode() {
      return localName != null
              ? namespaceURI.hashCode() ^ localName.hashCode()
              : name.hashCode();
    }
  }
  
  Map<NSName,Node> map = new HashMap<NSName,Node>();
  
  Node[] cache;

  public Node getNamedItem(String name) {
    return map.get(new NSName(name));
  }

  public Node setNamedItem(Node node) throws DOMException {
    cache = null;
    NSName name = new NSName(node.getNodeName());
    return map.put(name, node);
  }

  public Node removeNamedItem(String name) throws DOMException {
    cache = null;
    return map.remove(new NSName(name));
  }

  public Node item(int i) {
    if (cache == null)
      cache = map.values().toArray(new Node[map.size()]);
    return cache[i];
  }

  public int getLength() {
    return map.size();
  }

  public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
    return map.get(new NSName(namespaceURI, localName));
  }

  public Node setNamedItemNS(Node node) throws DOMException {
    cache = null;
    NSName name = new NSName(node.getNamespaceURI(), node.getLocalName());
    return map.put(name, node);
  }

  public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
    cache = null;
    NSName name = new NSName(namespaceURI, localName);
    return map.remove(name);
  }
}

