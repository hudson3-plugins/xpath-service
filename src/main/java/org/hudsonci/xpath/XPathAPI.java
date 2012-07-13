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

import org.hudsonci.xpath.XNamespaceContext;
import org.hudsonci.xpath.XVariableContext;
import java.util.List;
import org.dom4j.Node;
import org.hudsonci.xpath.XPathException;

/**
 *
 * @author bobfoster
 */
public interface XPathAPI {
  
  public void setVariableContext(XVariableContext varContext);
  
  public XVariableContext getVariableContext();
  
  public void setNamespaceContext(XNamespaceContext nsContext);
  
  public XNamespaceContext getNamespaceContext();
  
  public Object evaluate(Object xpathContext) throws XPathException;
  
  public boolean booleanValueOf(Object xpathContext) throws XPathException;

  public double numberValueOf(Object xpathContext) throws XPathException;

  public String stringValueOf(Object xpathContext) throws XPathException;

  public Node selectSingleNode(Object xpathContext) throws XPathException;
  
  public List selectNodes(Object xpathContext) throws XPathException;
  
  public String toString();
  
  public void setExpr(String expr);
}
