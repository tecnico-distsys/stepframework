package step.framework.ws;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.Node;

/**
 *  This class contains XML-related utility methods.
 */
public class XMLUtil {

    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private static final boolean DEFAULT_OMIT = true;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final boolean DEFAULT_INDENT = true;
    private static final int DEFAULT_INDENT_AMOUNT = 2;

    /** 
     *  Print XML source to XML result using specified option parameters.
     */
    public static void prettyPrint(Source sourceXml, Result resultXml,
                                   boolean omitXmlDeclaration, String encoding, boolean indent, int indentAmount)
        throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {

        Transformer tf = transformerFactory.newTransformer();

        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, String.valueOf(omitXmlDeclaration));
        if(encoding != null)
            tf.setOutputProperty(OutputKeys.ENCODING, encoding);
        tf.setOutputProperty(OutputKeys.INDENT, String.valueOf(indent));
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
        
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

}
