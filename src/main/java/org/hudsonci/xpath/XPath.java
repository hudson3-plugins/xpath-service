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

import java.util.List;
import java.util.ServiceLoader;
import org.dom4j.Node;
import org.hudsonci.xpath.impl.XPathJavaImpl;

/**
 *
 * @author Bob Foster
 */
public class XPath implements XPathAPI {
  
  XPathAPI impl;
  
  private static XPathAPIFactory factory = null;
  
  public static void provideXPathService(XPathAPIFactory factory) {
    XPath.factory = factory;
  }
  
  public XPath(String expr) throws XPathException {
    
    if (factory != null)
      impl = factory.newXPathAPI(expr);
    
    // If no service provider, fall back on default implementation
    // NB: If the default implementation was robust, we wouldn't
    // need the service!
    if (impl == null)
      impl = new XPathJavaImpl(expr);
  }
  
  public void setVariableContext(XVariableContext varContext) {
    impl.setVariableContext(varContext);
  }
  
  public XVariableContext getVariableContext() {
    return impl.getVariableContext();
  }
  
  public void setNamespaceContext(XNamespaceContext nsContext) {
    impl.setNamespaceContext(nsContext);
  }
  
  public XNamespaceContext getNamespaceContext() {
    return impl.getNamespaceContext();
  }
  
  public Object evaluate(Object xpathContext) throws XPathException {
    return impl.evaluate(xpathContext);
  }
  
  public boolean booleanValueOf(Object xpathContext) throws XPathException {
    return impl.booleanValueOf(xpathContext);
  }

  public double numberValueOf(Object xpathContext) throws XPathException {
    return impl.numberValueOf(xpathContext);
  }

  public String stringValueOf(Object xpathContext) throws XPathException {
    return impl.stringValueOf(xpathContext);
  }

  public Node selectSingleNode(Object xpathContext) throws XPathException {
    return impl.selectSingleNode(xpathContext);
  }
  
  public List selectNodes(Object xpathContext) throws XPathException {
    return impl.selectNodes(xpathContext);
  }
  
  public String toString() {
    return impl.toString();
  }
  
  public Object getImpl() {
    return impl;
  }
  
  public void setExpr(String expr) {
    impl.setExpr(expr);
  }
}
