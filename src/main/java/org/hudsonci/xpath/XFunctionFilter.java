/*******************************************************************************
 *
 * Copyright (c) 2015 Oracle Corporation.
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

/**
 *
 * @author Bob Foster
 */
public interface XFunctionFilter {

    boolean accept(String namespaceURI, String prefix, String localName);
}
