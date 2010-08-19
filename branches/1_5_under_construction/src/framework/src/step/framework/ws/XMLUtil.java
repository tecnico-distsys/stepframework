package step.framework.ws;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;


/**
 *  This class contains XML-related utility methods.
 */
public class XMLUtil {

    //
    //  Pretty Print
    //

    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private static final boolean DEFAULT_OMIT = true;
    private static final String DEFAULT_ENCODING = null;
    private static final boolean DEFAULT_INDENT = true;
    private static final int DEFAULT_INDENT_AMOUNT = 2;

    /**
     *  Print XML source to XML result using specified option parameters.
     */
    public static void prettyPrint(Source sourceXml, Result resultXml,
                                   boolean omitXmlDeclaration, String encoding, boolean indent, int indentAmount)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        Transformer tf = transformerFactory.newTransformer();

        if(omitXmlDeclaration)
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        if(encoding != null)
            tf.setOutputProperty(OutputKeys.ENCODING, encoding);
        if(indent) {
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
        }

        tf.transform(sourceXml, resultXml);
    }

    /**
     *  Print XML source to XML result using default indent options.
     */
    public static void prettyPrint(Source sourceXml, Result resultXml)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        prettyPrint(sourceXml, resultXml,
                    DEFAULT_OMIT, DEFAULT_ENCODING, DEFAULT_INDENT, DEFAULT_INDENT_AMOUNT);
    }

    /**
     *  Print XML node to output stream using specified option parameters.
     */
    public static void prettyPrint(Node xmlNode, OutputStream out,
                                   boolean omitXmlDeclaration, String encoding, boolean indent, int indentAmount)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        prettyPrint(new DOMSource(xmlNode), new StreamResult(out),
                    omitXmlDeclaration, encoding, indent, indentAmount);
    }

    /**
     *  Print XML node to output stream using default indent options.
     */
    public static void prettyPrint(Node xmlNode, OutputStream out)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        prettyPrint(xmlNode, out,
                    DEFAULT_OMIT, DEFAULT_ENCODING, DEFAULT_INDENT, DEFAULT_INDENT_AMOUNT);
    }

    /**
     *  Print XML string to output stream using specified option parameters.
     */
    public static void prettyPrint(String xmlString, OutputStream out,
                                   boolean omitXmlDeclaration, String encoding, boolean indent, int indentAmount)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        prettyPrint(new StreamSource(new StringReader(xmlString)), new StreamResult(out),
                    omitXmlDeclaration, encoding, indent, indentAmount);
    }

    /**
     *  Print XML string to output stream using default indent options.
     */
    public static void prettyPrint(String xmlString, OutputStream out)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        prettyPrint(xmlString, out,
                    DEFAULT_OMIT, DEFAULT_ENCODING, DEFAULT_INDENT, DEFAULT_INDENT_AMOUNT);
    }


    //
    //  DOM Navigation helpers
    //

    /**
     *  Obtain first child element from provided node.
     *  Other nodes (like text, comments, etc) are ignored.
     */
    public static Element getFirstChildElement(Node parent) {
        return getFirstChildElement((Element)parent);
    }

    /**
     *  Obtain first child element from provided element.
     *  Other nodes (like text, comments, etc) are ignored.
     *  Returns null if no child element is found.
     */
    public static Element getFirstChildElement(Element parent) {
        for(Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) node;
            }
        }
        // no child element found
        return null;
    }


    //
    //  XML Metrics
    //

    /** Inner class to hold XML metrics computed from a document */
    public static class XMLMetrics {
        /** Number of XML nodes (excluding attributes) */
        public int nodeCount = 0;

        /** Number of XML element nodes */
        public int elemCount = 0;
        /** Total element name text length (in characters) */
        public int elemNameLen = 0;

        /** Number of XML text nodes */
        public int textCount = 0;
        /** Total text length (in characters) */
        public int textLen = 0;

        /** Number of XML attributes */
        public int attrCount = 0;
        /** Total attribute name text length (in characters) */
        public int attrNameLen = 0;
        /** Total attribute value text length (in characters) */
        public int attrValueLen = 0;

        /** Maximum depth. A single node document has depth 1. */
        public int maxDepth = 0;

        /** The logical length is the sum of
         *  the length of element names, text length, attribute names, and attribute values.
         *  The unit is 'characters' (not bytes).
         *
         *  The element name is counted only once even if there is an opening and closing tag
         *  e.g. logical length of <root></root> is 4 and not 8 characters.  */
        public int getLogicalLength() { return elemNameLen + textLen + attrNameLen + attrValueLen; }

        /** report string */
        public String toString() {
            final String FORMAT = "XML document has a logical length of %d with " +
                "%d nodes " +
                "(%d elements with length %d, " +
                "%d texts with length %d, " +
                "%d attributes with length %d and value length %d, " +
                "%d others) " +
                "and with a maximum depth of %d.";
            return String.format(FORMAT, getLogicalLength(),
                nodeCount,
                elemCount, elemNameLen,
                textCount, textLen,
                attrCount, attrNameLen, attrValueLen,
                (nodeCount - elemCount - textCount - attrCount),
                maxDepth);
        }

        /** compact report string */
        public String toCompactString() {
            final String FORMAT = "ll:%d nc:%d md:%d";
            return String.format(FORMAT, getLogicalLength(),
                nodeCount,
                maxDepth);
        }
    }

    /** Compute XML Metrics for the given XML tree */
    public static XMLMetrics computeMetrics(Node xmlNode) {
        // create result object
        XMLMetrics metrics = new XMLMetrics();

        // null node has 0 metrics
        if (xmlNode == null)
            return metrics;

        // depth is 1 because at least 1 level exists (root)
        int depth = 1;
        metrics.maxDepth = depth;

        final Node ROOT = xmlNode;
        Node currentNode = ROOT;
        Node lastNode = null;

        while (currentNode != null) {
            Node firstChild = currentNode.getFirstChild();
            Node lastChild = currentNode.getLastChild();

            // at root note (node count is 0) or seeing a node for the first time)
            if (metrics.nodeCount == 0 || lastNode != lastChild) {
                metrics.nodeCount++;
                //System.out.println("Counting " + currentNode.getNodeName() + " element");

                // element node
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    metrics.elemCount++;
                    metrics.elemNameLen += currentNode.getNodeName().length();

                    // handle attributes
                    NamedNodeMap attrs = currentNode.getAttributes();
                    int attrsLength = attrs.getLength();
                    metrics.attrCount += attrsLength;
                    metrics.nodeCount += attrsLength;
                    for (int i=0; i < attrsLength; i++) {
                        Node attr = attrs.item(i);
                        //System.out.println("Counting " + attr.getNodeName() + " attribute");
                        metrics.attrNameLen += attr.getNodeName().length();
                        metrics.attrValueLen += attr.getNodeValue().length();
                    }

                // text node
                } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
                    //System.out.println("Counting '" + currentNode.getNodeValue() + "' text");
                    metrics.textCount++;
                    metrics.textLen += currentNode.getNodeValue().length();
                }
            }

            if (firstChild != null && lastNode != lastChild) {
                //System.out.println("descend");
                depth++;
                if (depth > metrics.maxDepth) metrics.maxDepth = depth;
                lastNode = currentNode;
                currentNode = firstChild;
                continue;
            } else {
                Node nextSibling = currentNode.getNextSibling();
                if (nextSibling != null) {
                    //System.out.println("advance");
                    lastNode = currentNode;
                    currentNode = nextSibling;
                    continue;
                } else {
                    //System.out.println("ascend");
                    depth--;
                    Node parent = currentNode.getParentNode();
                    lastNode = currentNode;
                    currentNode = parent;
                    if (parent == ROOT) {
                        // reached initial node. End.
                        break;
                    } else {
                        continue;
                    }
                }
            }

        } // while

        return metrics;
    }


}
