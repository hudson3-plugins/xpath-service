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

import javax.xml.xpath.XPathException;

/**
 *
 * @author Bob Foster
 */
public interface XVariableContext {

  Object getVariableValue(String namespaceURI, String prefix, String localName) throws Exception;
}
