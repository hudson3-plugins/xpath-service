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

import org.dom4j.rule.Action;
import org.dom4j.rule.Rule;
import org.hudsonci.xpath.XPath;

/**
 *
 * @author bobfoster
 */
public interface StylesheetAPI {
  
  public void applyTemplates(Object source, XPath select, String mode) throws Exception;

  public void applyTemplates(Object source, String mode) throws Exception;

  public void setValueOfAction(Action valueOfAction);

  public void run(Object input) throws Exception;
  
  public void addRule(Rule rule);
  
  public void setModeName(String modeName);
}
