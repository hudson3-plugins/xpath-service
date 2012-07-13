/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hudsonci.xpath.impl;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.dom4j.Branch;
import org.dom4j.Node;
import org.dom4j.Text;

/**
 *
 * @author Bob Foster
 */
public class Trim {

  private static Map<Node,Node> trimmed = new WeakHashMap<Node,Node>();
  
  
  public static Node trimNode(Node node) {
    if (node instanceof Branch)
      trimBranch((Branch) node);
    return node;
  }
  
  public static void trimBranch(Branch branch) {
    // This does not normalize whitespace!
    // The document tree is modified in place, but no nodes are removed.
    if (trimmed.get(branch) != null) return;
    trimmed.put(branch, branch);
    List content = branch.content();
    // trim leading whitespace
    // NB: dom4j can have multiple consecutive Text nodes
    boolean first = true;
    for (int i = 0, n = content.size(); i < n; i++) {
      Object obj = content.get(i);
      if (obj instanceof Text) {
        Text text = (Text) obj;
        if (first) {
          String s = trimLeading(text.getText());
          text.setText(s);
          if (s.length() > 0)
            first = false;
        }
      } else {
        first = false;
        if (obj instanceof Branch)
          trimBranch((Branch) obj);
      }
    }
    // trim trailing whitespace
    for (int i = content.size()-1; i >= 0; i--) {
      Object obj = content.get(i);
      if (obj instanceof Text) {
        Text text = (Text) obj;
        String s = trimTrailing(text.getText());
        text.setText(s);
        if (s.length() > 0)
          break;
      } else
        break;
    }
  }
  
  private static boolean isWhitespace(char c) {
    // TODO Check definition
    return c == ' ' || c == '\t' || c == '\r' || c == '\n';
  }
  
  private static String trimLeading(String s) {
    int m = s.length();
    for (int i = 0, n = m; i < n; i++) {
      if (!isWhitespace(s.charAt(i))) {
        m = i;
        break;
      }
    }
    return s.substring(m);
  }
  
  private static String trimTrailing(String s) {
    int m = -1;
    for (int max = s.length()-1, i = max; i >= 0; i--) {
      if (!isWhitespace(s.charAt(i))) {
        m = i;
        break;
      }
    }
    return s.substring(0, m+1);
  }
  
}
