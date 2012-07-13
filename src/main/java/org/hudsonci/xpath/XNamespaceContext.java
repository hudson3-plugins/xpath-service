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

import java.util.*;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Bob Foster
 */
public class XNamespaceContext implements NamespaceContext {

  Map map;
  PrefixMap prefixMap;
  
  private static class PrefixMap {
    Map<String,List<String>> map = new HashMap<String,List<String>>();
        
    PrefixMap(Map nsMap) {
      for (Object obj : nsMap.entrySet()) {
        Map.Entry entry = (Map.Entry) obj;
        String prefix = (String) entry.getKey();
        String ns     = (String) entry.getValue();
        List<String> list = map.get(ns);
        if (list == null) {
          list = new ArrayList<String>();
          map.put(ns, list);
        }
        list.add(prefix);
      }
    }
    
    List<String> getPrefixes(String ns) {
      List<String> list = map.get(ns);
      if (list == null)
        return Collections.EMPTY_LIST;
      return list;
    }
    
  }
  
  public XNamespaceContext(Map uris) {
    this.map = uris;
  }

  public String getNamespaceURI(String prefix) {
    return (String) map.get(prefix);
  }
  
  private PrefixMap getPrefixMap() {
    if (prefixMap == null)
      prefixMap = new PrefixMap(map);
    return prefixMap;
  }

  public String getPrefix(String namespaceURI) {
     List<String> list = getPrefixMap().getPrefixes(namespaceURI);
     if (list.size() > 0)
       return list.get(0);
     return null;
  }

  public Iterator getPrefixes(String namespaceURI) {
    return getPrefixMap().getPrefixes(namespaceURI).iterator();
  }
}
