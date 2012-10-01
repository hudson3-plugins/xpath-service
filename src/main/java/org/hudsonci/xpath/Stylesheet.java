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

import java.util.ServiceLoader;
import org.dom4j.rule.Action;
import org.dom4j.rule.Rule;
import org.hudsonci.xpath.impl.StylesheetJavaImpl;

/**
 *
 * @author Bob Foster
 */
public class Stylesheet implements StylesheetAPI {

  StylesheetAPI impl;
  
  private static StylesheetAPIFactory factory = null;
  
  public static void provideStylesheetService(StylesheetAPIFactory factory) {
    Stylesheet.factory = factory;
  }
  
  public Stylesheet() {
    if (factory != null)
      impl = factory.newStylesheetAPI();
    
    // If no service provider (yet) use the default implementation.
    // NB: the default implementation does NOT work.
    
    if (impl == null)
      impl = new StylesheetJavaImpl();
  }

  public void applyTemplates(Object source, XPath select, String mode) throws Exception {
    impl.applyTemplates(source, select, mode);
  }

  public void applyTemplates(Object source, String mode) throws Exception {
    impl.applyTemplates(source, mode);
  }

  public void setValueOfAction(Action valueOfAction) {
    impl.setValueOfAction(valueOfAction);
  }

  public void run(Object input) throws Exception {
    impl.run(input);
  }
  
  public void addRule(Rule rule) {
    impl.addRule(rule);
  }
  
  public void setModeName(String modeName) {
    impl.setModeName(modeName);
  }
  
}
