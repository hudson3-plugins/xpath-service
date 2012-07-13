/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hudsonci.xpath.impl;

import org.hudsonci.xpath.StylesheetAPI;
import org.dom4j.rule.Action;
import org.dom4j.rule.Rule;
import org.hudsonci.xpath.XPath;

/**
 *
 * @author Bob Foster
 */
public class StylesheetJavaImpl implements StylesheetAPI {

  public StylesheetJavaImpl() {
    
  }
  
  public void applyTemplates(Object source, XPath select, String mode) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void applyTemplates(Object source, String mode) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setValueOfAction(Action valueOfAction) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void run(Object input) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addRule(Rule rule) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setModeName(String modeName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
