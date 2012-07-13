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

package org.hudsonci.xpath.impl;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Node;
import org.hudsonci.xpath.XNamespaceContext;
import org.hudsonci.xpath.XPathException;
import org.hudsonci.xpath.XVariableContext;

/**
 * Rewrite an XPath expression containing Node variables.
 * 
 * @author Bob Foster
 */
public class Rewriter {
  
  XVariableContext varContext;
  XNamespaceContext nsContext;

  /**
   * Expr represents a sub-expression of an XPath expression.
   * There are only two sub-types, Var and Str, representing
   * an XPath variable and anything else, respectively.
   */
  private abstract class Expr {
    Expr next;
    Expr() {
    }
    void collectNodes(List<Node> list) throws XPathException {
      if (next != null)
        next.collectNodes(list);
    }
    void replaceNodes(Node dominant) {
      if (next != null)
        next.replaceNodes(dominant);
    }
    public String toString() {
      StringBuilder sb = new StringBuilder();
      collectString(sb);
      return sb.toString();
    }
    abstract void collectString(StringBuilder sb);
  }
  
  /**
   * XPath variable.
   */
  private class Var extends Expr {
    String var;
    Object value;
    String replacement;
    Var(String var) {
      this.var = var;
    }
    void collectNodes(List<Node> list) throws XPathException {
      String ns = "";
      String px = "";
      String ln = var;
      if (nsContext != null) {
        int colon = var.indexOf(':');
        if (colon >= 0) {
          px = var.substring(0, colon);
          ln = var.substring(colon+1);
          ns = nsContext.getNamespaceURI(px);
        }
      }
      try {
      value = varContext.getVariableValue(ns, px, ln);
      if (value instanceof Node)
        list.add((Node) value);
      if (next != null)
        next.collectNodes(list);
      } catch (Exception e) {
        throw new XPathException(e);
      }
    }
    void collectString(StringBuilder sb) {
      sb.append(replacement);
      if (next != null)
        next.collectString(sb);
    }
    void replaceNodes(Node dominant) {
      if (value == dominant)
        replacement = ".";
      else
        // For non-dominant nodes, the reasoning is the $var will work
        // ok, as the dominant node establishes the context of evaluation.
        replacement = "$"+var;
      if (next != null)
        next.replaceNodes(dominant);
    }
  }
  
  /**
   * Anything but an XPath variable.
   */
  private class Str extends Expr {
    String str;
    Str(String str) {
      this.str = str;
    }
    void collectString(StringBuilder sb) {
      sb.append(str);
      if (next != null)
        next.collectString(sb);
    }
  }
  
  Expr first;
  Expr last;
  
  void addExpr(Expr e) {
    if (first == null)
      first = last = e;
    else {
      last.next = e;
      last = e;
    }
  }
  
  /** Characters in an XPath expression that can't be part of a variable name. */
  static boolean[] nonVar = new boolean[255];
  static {
    nonVar[' '] = nonVar['\t'] = nonVar['\n'] = nonVar['\r'] = true;
    nonVar['('] = nonVar[')'] = nonVar['['] = nonVar[']'] = true;
    nonVar['+'] = nonVar['-'] = nonVar['*'] = nonVar['|'] = true;
    nonVar['='] = nonVar['!'] = nonVar['>'] = nonVar['<'] = true;
    nonVar['/'] = nonVar['$'] = nonVar['?'] = true;
    nonVar['.'] = nonVar[','] = true;
  }
  
  /**
   * @param c
   * @return true if c is not a valid variable character (a much simpler
   * question to answer than if c <i>is</i> a valid character)
   */
  private boolean endOfVar(char c) {
    return c > 255 || nonVar[c];
  }
  
  /**
   * Find the dominant node in a list of more than one Node.
   * @param nodes list with more than one node
   * @return the dominant node (node that contains the others)
   * @throws Exception 
   */
  private Node findDominantNode(List<Node> nodes) throws XPathException {
    Node dominant = null;
    for (Node node : nodes) {
      if (dominant == null)
        dominant = node;
      else
        dominant = findDominantNode(dominant, node);
    }
    if (dominant == null) throw new XPathException("No dominant node found in expression");
    return dominant;
  }
  
  /**
   * @param node1
   * @param node2
   * @return the node that is or contains the other
   * @throws Exception 
   */
  private Node findDominantNode(Node node1, Node node2) {
    // Fast tests
    if (node1 == node2)
      return node1;
    // Both nodes come from same document
    Document doc = node1.getDocument();
    if (node1 == doc)
      return node1;
    if (node2 == doc)
      return node2;
    // Slow tests
    if (nodeContains(node1, node2))
      return node1;
    if (nodeContains(node2, node1))
      return node2;
    return null;
  }
  
  private boolean nodeContains(Node parent, Node node) {
    if (parent == node)
      return true;
    if (parent instanceof Branch) {
      Branch branch = (Branch) parent;
      List children = branch.content();
      for (int i = 0, n = children.size(); i < n; i++) {
        boolean contains = nodeContains((Node)children.get(i), node);
        if (contains)
          return true;
      }
    }
    return false;
  }
  
  /**
   * Rewrite the XPath expression such that at least one Node-valued
   * variable is replaced by the outer-most context ".".
   * 
   * Should only be called if the XPath evaluation context is null.
   * 
   * @param expr XPath expression
   * @param varContext context for variables
   * @param nsContext context for namespaces
   * @return Pair<rewritten-expression, context-node>
   * @throws XPathException if no Node-valued variable that is a suitable
   * context is found
   */
  public Pair<String,Node> rewriteExpression(String expr, XVariableContext varContext, XNamespaceContext nsContext) throws XPathException {
    this.varContext = varContext;
    this.nsContext  = nsContext;
    
    String rewrittenExpression = expr;
    Node dominantNode = null;
    
    // Split into subexpressions
    first = null;
    last = null;
    int start = 0;
    for (int i = 0, n = expr.length(); i < n; i++) {
      char c = expr.charAt(i);
      if (c == '$') {
        if (i > start)
          addExpr(new Str(expr.substring(start, i)));
        // start of variable
        int j = start = i + 1;
        for (; j < n; j++) {
          char v = expr.charAt(j);
          if (endOfVar(v))
            break;
        }
        addExpr(new Var(expr.substring(start, j)));
        start = j;
        i = j - 1;
      }
    }
    if (start < expr.length())
      addExpr(new Str(expr.substring(start)));
    
    List<Node> nodes = new ArrayList<Node>();
    first.collectNodes(nodes);
    
    if (nodes.size() > 0) {
      dominantNode = nodes.get(0);

      if (nodes.size() > 1)
        dominantNode = findDominantNode(nodes);

      first.replaceNodes(dominantNode);

      rewrittenExpression = first.toString();
    }
    
    if (rewrittenExpression == null)
      throw new XPathException("Cannot rewrite expression to obtain a valid context node");
    
    return new Pair(rewrittenExpression, dominantNode);
  }
  
}
