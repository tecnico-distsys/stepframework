package step.framework.ws;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import org.junit.*;
import static org.junit.Assert.*;


public class XMLUtilTest {

    static final private String BR = System.getProperty("line.separator");
    private PrintStream out = System.out;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static final String COMPACT_XML =
        "<grandfather name=\"Tony\"><father name=\"Tony\">" +
        "<son name=\"Mike\"/><son name=\"John\"></son>" +
        "</father></grandfather>";

    private static final String INDENTED_XML =
        "<grandfather name=\"Tony\">" + BR +
        "  <father name=\"Tony\">" + BR +
        "    <son name=\"Mike\"/>" + BR +
        "    <son name=\"John\"/>" + BR +
        "  </father>" + BR +
        "</grandfather>" + BR;


    @Test
    public void testPrettyPrintString() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLUtil.prettyPrint(COMPACT_XML, baos);
        String actual = baos.toString();

        assertEquals(INDENTED_XML, actual);
    }

    private Document stringToDocument(String xml) throws Exception {
        // create empty target document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // convert text to DOM
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new StreamSource(new StringReader(xml)), new DOMResult(document));

        return document;
    }

    @Test
    public void testBuiltXMLMetrics() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(document.createElement("root"));

        if (false) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        }

        Node node = document.getDocumentElement();
        assertEquals("root", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("node count", 1, metrics.nodeCount);

        assertEquals("element node count", 1, metrics.elemCount);
        assertEquals("element name text length", 4, metrics.elemNameLen);

        assertEquals("text node count", 0, metrics.textCount);
        assertEquals("tex length", 0, metrics.textLen);

        assertEquals("attribute count", 0, metrics.attrCount);
        assertEquals("attribute name text length", 0, metrics.attrNameLen);
        assertEquals("attribute value text length", 0, metrics.attrValueLen);

        assertEquals("maximum depth", 1, metrics.maxDepth);

        assertEquals("logical length", 4, metrics.getLogicalLength());
    }

    @Test
    public void testCompactXMLMetrics() throws Exception {
        Document document = stringToDocument(COMPACT_XML);

        if (false) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        }

        Node node = document.getDocumentElement();
        assertEquals("grandfather", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("node count", 8, metrics.nodeCount);

        assertEquals("element node count", 4, metrics.elemCount);
        assertEquals("element name text length", 11+6+(3*2), metrics.elemNameLen);

        assertEquals("text node count", 0, metrics.textCount);
        assertEquals("tex length", 0, metrics.textLen);

        assertEquals("attribute count", 4, metrics.attrCount);
        assertEquals("attribute name text length", 4*4, metrics.attrNameLen);
        assertEquals("attribute value text length", 4*4, metrics.attrValueLen);

        assertEquals("maximum depth", 3, metrics.maxDepth);

        assertEquals("logical length", (11+6+(3*2)) + 0 + 4*4 + 4*4, metrics.getLogicalLength());
    }

    @Test
    public void testFirstChildCompactXMLMetrics() throws Exception {
        Document document = stringToDocument(COMPACT_XML);

        if (false) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        }

        Node node = document.getDocumentElement().getFirstChild();
        assertEquals("father", node.getNodeName());

        // compute metrics for first child element
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("node count", 6, metrics.nodeCount);

        assertEquals("element node count", 3, metrics.elemCount);
        assertEquals("element name text length", 6+(3*2), metrics.elemNameLen);

        assertEquals("text node count", 0, metrics.textCount);
        assertEquals("tex length", 0, metrics.textLen);

        assertEquals("attribute count", 3, metrics.attrCount);
        assertEquals("attribute name text length", 3*4, metrics.attrNameLen);
        assertEquals("attribute value text length", 3*4, metrics.attrValueLen);

        assertEquals("maximum depth", 2, metrics.maxDepth);

        assertEquals("logical length", (6+(3*2)) + 0 + 3*4 + 3*4, metrics.getLogicalLength());
    }


    @Test
    public void testIndentedXMLMetrics() throws Exception {
        Document document = stringToDocument(INDENTED_XML);

        if (false) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        }

        Node node = document.getDocumentElement();
        assertEquals("grandfather", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("node count", 4+4+5, metrics.nodeCount);

        assertEquals("element node count", 4, metrics.elemCount);
        assertEquals("element name text length", 11+6+(3*2), metrics.elemNameLen);

        assertEquals("text node count", 5, metrics.textCount);
        assertEquals("tex length", 17, metrics.textLen);

        assertEquals("attribute count", 4, metrics.attrCount);
        assertEquals("attribute name text length", 4*4, metrics.attrNameLen);
        assertEquals("attribute value text length", 4*4, metrics.attrValueLen);

        assertEquals("maximum depth", 3, metrics.maxDepth);

        assertEquals("logical length", (11+6+(3*2)) + 17 + 4*4 + 4*4, metrics.getLogicalLength());

    }

    @Test
    public void testFirstChildElementIndentedXMLMetrics() throws Exception {
        Document document = stringToDocument(INDENTED_XML);

        if (false) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        }

        Node node = document.getDocumentElement();
        assertEquals("grandfather", node.getNodeName());

        for (node = node.getFirstChild();
            node.getNodeType() != Node.ELEMENT_NODE;
            node = node.getNextSibling());

        assertNotNull(node);
        assertEquals("father", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("node count", 3+3+3, metrics.nodeCount);

        assertEquals("element node count", 3, metrics.elemCount);
        assertEquals("element name text length", 6+(3*2), metrics.elemNameLen);

        assertEquals("text node count", 3, metrics.textCount);
        assertEquals("tex length", 13, metrics.textLen);

        assertEquals("attribute count", 3, metrics.attrCount);
        assertEquals("attribute name text length", 3*4, metrics.attrNameLen);
        assertEquals("attribute value text length", 3*4, metrics.attrValueLen);

        assertEquals("maximum depth", 2, metrics.maxDepth);

        assertEquals("logical length", (6+(3*2)) + 13 + 3*4 + 3*4, metrics.getLogicalLength());
    }

    @Test
    public void testToString() throws Exception {
        Document document = stringToDocument(INDENTED_XML);
        Node node = document.getDocumentElement();
        assertEquals("grandfather", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        final String EXPECTED = "XML document has a logical length of 72 with " +
            "13 nodes " +
            "(4 elements with length 23, " +
            "5 texts with length 17, " +
            "4 attributes with length 16 and value length 16, " +
            "0 others) " +
            "and with a maximum depth of 3.";

        assertEquals(EXPECTED, metrics.toString());
    }

    @Test
    public void testToCompactString() throws Exception {
        Document document = stringToDocument(COMPACT_XML);
        Node node = document.getDocumentElement();
        assertEquals("grandfather", node.getNodeName());

        // compute metrics
        XMLUtil.XMLMetrics metrics = XMLUtil.computeMetrics(node);

        // assert results
        assertEquals("ll:55 nc:8 md:3", metrics.toCompactString());
    }

}
