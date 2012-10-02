xpath-service
=============

Provides XPath and Stylesheet features for Hudson and Jelly (which no longer
contain Jaxen).

Converting dom4j XPath-related calls to XPath service is usually trivial, e.g.,

    doc.selectNodes(pathExpr)

becomes

    new XPath(pathExpr).selectNodes(doc)


