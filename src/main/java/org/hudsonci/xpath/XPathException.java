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

/**
 *
 * @author Bob Foster
 */
public class XPathException extends Exception {

  public XPathException(String msg) {
    super(msg);
  }
  
  public XPathException(Throwable t) {
    super(t);
  }
}
